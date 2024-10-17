package cc.uncarbon.module.adminapi.model.response;

import cc.uncarbon.framework.core.enums.HelioBaseEnum;
import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * 后台管理-下拉框数据单项 VO
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
@Getter
public class AdminSelectOptionItemVO implements Serializable {
    /**
     * Jackson等序列化框架，可利用此无参构造器反射生成对象
     */
    private AdminSelectOptionItemVO() {
    }

    // ID👉名称 一对（用于关联各种实体）
    @Schema(description = "ID")
    private Number id;
    @Schema(description = "名称")
    private String name;

    public AdminSelectOptionItemVO(Number id, String name) {
        this.id = id;
        this.name = name;
    }

    // 有时候额外需要上级ID
    @Schema(description = "上级ID")
    @Setter
    private Number parentId;

    public AdminSelectOptionItemVO(Number id, String name, Number parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }


    // 值👉标签 一对（仅用于枚举）
    @Schema(description = "值")
    private Number value;
    @Schema(description = "标签")
    private String label;

    public AdminSelectOptionItemVO(HelioBaseEnum<? extends Number> helioBaseEnum) {
        this.value = helioBaseEnum.getValue();
        this.label = helioBaseEnum.getLabel();
    }

    /*
    ----------------------------------------------------------------
                        自定义业务字段都写在这里
                        都要标记释义、用处、新增时版本号
                        免得每个人各取一个名，不统一
    ----------------------------------------------------------------
     */



    /*
    ----------------------------------------------------------------
                        构造方法 builders
    ----------------------------------------------------------------
     */

    /**
     * 构造List<AdminSelectOptionItemVO>
     * 将转换源集合中所有集合项
     * 无需上级ID
     *
     * @param source     源集合
     * @param idGetter   id getter
     * @param nameGetter name getter
     */
    public static <T> List<AdminSelectOptionItemVO> listOf(
            Collection<T> source,
            @NonNull Function<T, Number> idGetter,
            @NonNull Function<T, String> nameGetter
    ) {
        return listOf(source, idGetter, nameGetter, null, null, null);
    }

    /**
     * 构造List<AdminSelectOptionItemVO>
     * 将转换源集合中所有集合项
     * 支持上级ID
     *
     * @param source         源集合
     * @param idGetter       id getter
     * @param nameGetter     name getter
     * @param parentIdGetter 上级ID getter
     */
    public static <T> List<AdminSelectOptionItemVO> listOf(
            Collection<T> source,
            @NonNull Function<T, Number> idGetter,
            @NonNull Function<T, String> nameGetter,
            Function<T, Number> parentIdGetter
    ) {
        return listOf(source, idGetter, nameGetter, parentIdGetter, null, null);
    }

    /**
     * 构造List<AdminSelectOptionItemVO>
     * 将转换源集合中所有集合项
     * 无需上级ID
     *
     * @param source                   源集合
     * @param idGetter                 id getter
     * @param nameGetter               name getter
     * @param postConversionProcessing （可选）转换后置处理过程，方便加入一些自定义字段，如 code、quantity 等
     */
    public static <T> List<AdminSelectOptionItemVO> listOf(
            Collection<T> source,
            @NonNull Function<T, Number> idGetter,
            @NonNull Function<T, String> nameGetter,
            BiConsumer<T, AdminSelectOptionItemVO> postConversionProcessing
    ) {
        return listOf(source, idGetter, nameGetter, null, null, postConversionProcessing);
    }

    /**
     * 构造List<AdminSelectOptionItemVO>
     * 支持自定义过滤器，仅转换需要的集合项
     * 支持上级ID
     *
     * @param source                   源集合
     * @param idGetter                 id getter
     * @param nameGetter               name getter
     * @param parentIdGetter           （可选）parentId getter
     * @param sourceItemFilter         （可选）集合项过滤器
     * @param postConversionProcessing （可选）转换后置处理过程，方便加入一些自定义字段，如 code、quantity 等
     */
    public static <T> List<AdminSelectOptionItemVO> listOf(
            Collection<T> source,
            @NonNull Function<T, Number> idGetter,
            @NonNull Function<T, String> nameGetter,
            Function<T, Number> parentIdGetter,
            Predicate<T> sourceItemFilter,
            BiConsumer<T, AdminSelectOptionItemVO> postConversionProcessing
    ) {
        if (CollUtil.isEmpty(source)) {
            return Collections.emptyList();
        }
        Stream<T> stream = source.stream();
        if (sourceItemFilter != null) {
            stream = stream.filter(sourceItemFilter);
        }

        return stream.map(sourceItem -> {
                    AdminSelectOptionItemVO optionItem = new AdminSelectOptionItemVO(idGetter.apply(sourceItem), nameGetter.apply(sourceItem));
                    if (Objects.nonNull(parentIdGetter)) {
                        optionItem.setParentId(parentIdGetter.apply(sourceItem));
                    }
                    if (Objects.nonNull(postConversionProcessing)) {
                        postConversionProcessing.accept(sourceItem, optionItem);
                    }
                    return optionItem;
                }).toList();
    }

    /**
     * 构造List<AdminSelectOptionItemVO>
     * 将转换枚举类中所有枚举常量
     *
     * @param helioBaseEnum 实现了HelioBaseEnum的枚举类
     */
    public static <E extends Enum<?> & HelioBaseEnum<? extends Number>> List<AdminSelectOptionItemVO> listOf(Class<E> helioBaseEnum) {
        return listOf(helioBaseEnum, null);
    }

    /**
     * 构造List<AdminSelectOptionItemVO>
     * 支持自定义过滤器，仅转换需要的枚举常量
     *
     * @param helioBaseEnum      实现了HelioBaseEnum的枚举类
     * @param enumConstantFilter （可选）枚举类中枚举常量过滤器
     */
    public static <E extends Enum<?> & HelioBaseEnum<? extends Number>> List<AdminSelectOptionItemVO> listOf(
            Class<E> helioBaseEnum,
            Predicate<E> enumConstantFilter
    ) {
        if (helioBaseEnum == null) {
            return Collections.emptyList();
        }
        Stream<E> stream = Arrays.stream(helioBaseEnum.getEnumConstants());
        if (enumConstantFilter != null) {
            stream = stream.filter(enumConstantFilter);
        }
        return stream.map(AdminSelectOptionItemVO::new).toList();
    }
}
