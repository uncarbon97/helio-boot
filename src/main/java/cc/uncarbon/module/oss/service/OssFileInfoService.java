package cc.uncarbon.module.oss.service;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.framework.core.page.PageParam;
import cc.uncarbon.framework.core.page.PageResult;
import cc.uncarbon.module.oss.constant.OssConstant;
import cc.uncarbon.module.oss.entity.OssFileInfoEntity;
import cc.uncarbon.module.oss.enums.OssErrorEnum;
import cc.uncarbon.module.oss.mapper.OssFileInfoMapper;
import cc.uncarbon.module.oss.model.request.AdminListOssFileInfoDTO;
import cc.uncarbon.module.oss.model.request.UploadFileAttributeDTO;
import cc.uncarbon.module.oss.model.response.OssFileInfoBO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * 上传文件信息
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OssFileInfoService {

    private final OssFileInfoMapper ossFileInfoMapper;
    private final FileStorageService fileStorageService;


    /**
     * 后台管理-分页列表
     */
    public PageResult<OssFileInfoBO> adminList(PageParam pageParam, AdminListOssFileInfoDTO dto) {
        Page<OssFileInfoEntity> entityPage = ossFileInfoMapper.selectPage(
                new Page<>(pageParam.getPageNum(), pageParam.getPageSize()),
                new QueryWrapper<OssFileInfoEntity>()
                        .lambda()
                        // 原始文件名
                        .like(CharSequenceUtil.isNotBlank(dto.getOriginalFilename()), OssFileInfoEntity::getOriginalFilename, CharSequenceUtil.cleanBlank(dto.getOriginalFilename()))
                        // 扩展名
                        .eq(CharSequenceUtil.isNotBlank(dto.getExtendName()), OssFileInfoEntity::getExtendName, CharSequenceUtil.cleanBlank(dto.getExtendName()))
                        // 文件类别
                        .eq(CharSequenceUtil.isNotBlank(dto.getClassified()), OssFileInfoEntity::getClassified, CharSequenceUtil.cleanBlank(dto.getClassified()))
                        // 时间区间
                        .between(ObjectUtil.isNotNull(dto.getBeginAt()) && ObjectUtil.isNotNull(dto.getEndAt()), OssFileInfoEntity::getCreatedAt, dto.getBeginAt(), dto.getEndAt())
                        // 排序
                        .orderByDesc(OssFileInfoEntity::getCreatedAt)
        );

        return this.entityPage2BOPage(entityPage);
    }

    /**
     * 根据 ID 取详情
     *
     * @param id 主键ID
     * @return null or BO
     */
    public OssFileInfoBO getOneById(Long id) {
        return this.getOneById(id, false);
    }

    /**
     * 根据 ID 取详情
     *
     * @param id               主键ID
     * @param throwIfInvalidId 是否在 ID 无效时抛出异常
     * @return null or BO
     */
    public OssFileInfoBO getOneById(Long id, boolean throwIfInvalidId) throws BusinessException {
        OssFileInfoEntity entity = ossFileInfoMapper.selectById(id);
        if (throwIfInvalidId) {
            OssErrorEnum.INVALID_ID.assertNotNull(entity);
        }

        return this.entity2BO(entity);
    }

    /**
     * 后台管理-删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminDelete(Collection<Long> ids) {
        log.info("[后台管理-删除上传文件信息] >> ids={}", ids);

        // 1. 删除原始文件
        List<OssFileInfoEntity> entityList = ossFileInfoMapper.selectBatchIds(ids);
        for (OssFileInfoEntity entity : entityList) {
            fileStorageService.delete(toFileInfo(entity));
        }

        // 2. 删除文件记录
        ossFileInfoMapper.deleteBatchIds(ids);
    }

    /**
     * 根据 MD5 取文件信息
     */
    public OssFileInfoBO getOneByMd5(String md5) {
        OssFileInfoEntity entity = ossFileInfoMapper.selectOne(
                new QueryWrapper<OssFileInfoEntity>()
                        .lambda()
                        .eq(OssFileInfoEntity::getMd5, md5)
                        .last(HelioConstant.CRUD.SQL_LIMIT_1)
        );

        return this.entity2BO(entity);
    }

    /**
     * 上传成功后，保存文件记录
     *
     * @return 文件ID
     */
    @Transactional(rollbackFor = Exception.class)
    public OssFileInfoBO save(@NonNull FileInfo fileInfo, @NonNull UploadFileAttributeDTO attr) {
        OssFileInfoEntity entity = new OssFileInfoEntity()
                .setStoragePlatform(fileInfo.getPlatform())
                .setStorageBasePath(fileInfo.getBasePath())
                .setStoragePath(fileInfo.getPath())
                // 需要裁切掉后缀名
                .setStorageFilename(FileUtil.getPrefix(fileInfo.getFilename()))
                .setOriginalFilename(FileUtil.getPrefix(fileInfo.getOriginalFilename()))
                .setExtendName(fileInfo.getExt())
                .setFileSize(fileInfo.getSize())
                .setMd5(attr.getMd5())
                .setClassified(attr.getClassified());

        if (!isLocalPlatform(fileInfo.getPlatform())) {
            // 非本地存储，保存对象存储直链
            entity.setDirectUrl(fileInfo.getUrl());
        }
        ossFileInfoMapper.insert(entity);

        return this.entity2BO(entity);
    }

    /**
     * 转换为 FileInfo 对象
     */
    public static FileInfo toFileInfo(OssFileInfoBO source) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setPlatform(source.getStoragePlatform());
        fileInfo.setBasePath(source.getStorageBasePath());
        fileInfo.setPath(source.getStoragePath());
        fileInfo.setFilename(source.getStorageFilenameFull());
        fileInfo.setSize(source.getFileSize());
        fileInfo.setOriginalFilename(source.getOriginalFilenameFull());
        return fileInfo;
    }

    /**
     * 转换为 FileInfo 对象
     */
    public static FileInfo toFileInfo(OssFileInfoEntity source) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setPlatform(source.getStoragePlatform());
        fileInfo.setBasePath(source.getStorageBasePath());
        fileInfo.setPath(source.getStoragePath());
        fileInfo.setFilename(source.getStorageFilenameFull());
        fileInfo.setSize(source.getFileSize());
        fileInfo.setOriginalFilename(source.getOriginalFilenameFull());
        return fileInfo;
    }

    /**
     * 是否为本地存储平台
     * @param storagePlatform 存储平台名
     */
    public static boolean isLocalPlatform(String storagePlatform) {
        return CharSequenceUtil.startWith(storagePlatform, OssConstant.PLATFORM_PREFIX_LOCAL);
    }

    /*
    ----------------------------------------------------------------
                        私有方法 private methods
    ----------------------------------------------------------------
     */

    /**
     * 实体转 BO
     *
     * @param entity 实体
     * @return BO
     */
    private OssFileInfoBO entity2BO(OssFileInfoEntity entity) {
        if (entity == null) {
            return null;
        }

        OssFileInfoBO bo = new OssFileInfoBO();
        BeanUtil.copyProperties(entity, bo);

        // 可以在此处为BO填充字段

        return bo;
    }

    /**
     * 实体 List 转 BO List
     *
     * @param entityList 实体 List
     * @return BO List
     */
    private List<OssFileInfoBO> entityList2BOs(List<OssFileInfoEntity> entityList) {
        if (CollUtil.isEmpty(entityList)) {
            return Collections.emptyList();
        }

        // 深拷贝
        List<OssFileInfoBO> ret = new ArrayList<>(entityList.size());
        entityList.forEach(
                entity -> ret.add(this.entity2BO(entity))
        );

        return ret;
    }

    /**
     * 实体分页转 BO 分页
     *
     * @param entityPage 实体分页
     * @return BO 分页
     */
    private PageResult<OssFileInfoBO> entityPage2BOPage(Page<OssFileInfoEntity> entityPage) {
        return new PageResult<OssFileInfoBO>()
                .setCurrent(entityPage.getCurrent())
                .setSize(entityPage.getSize())
                .setTotal(entityPage.getTotal())
                .setRecords(this.entityList2BOs(entityPage.getRecords()));
    }

}
