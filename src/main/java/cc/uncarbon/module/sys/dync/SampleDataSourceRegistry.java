package cc.uncarbon.module.sys.dync;

import cc.uncarbon.framework.core.constant.HelioConstant.CRUD;
import cc.uncarbon.framework.crud.dynamicdatasource.AbstractDataSourceRegistry;
import cc.uncarbon.framework.crud.dynamicdatasource.DataSourceDefinition;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.HikariDataSourceCreator;
import javax.annotation.PostConstruct;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 演示代码 -- 租户数据源登记处
 *
 * @author Uncarbon
 */
@Component
public class SampleDataSourceRegistry extends AbstractDataSourceRegistry {

    private static String DB_URL_101;
    private static String DB_URL_0;
    private static String DB_USERNAME_101;
    private static String DB_PASSWORD_101;

    private final Environment environment;

    public SampleDataSourceRegistry(DynamicRoutingDataSource dynamicRoutingDataSource,
            HikariDataSourceCreator dataSourceCreator, Environment environment) {
        super(dynamicRoutingDataSource, dataSourceCreator);
        this.environment = environment;
    }

    @PostConstruct
    public void postConstruct() {
        DB_URL_101 = environment.getProperty("DB_URL_101", String.class);
        DB_USERNAME_101 = environment.getProperty("DB_USERNAME_101", String.class);
        DB_PASSWORD_101 = environment.getProperty("DB_PASSWORD_101", String.class);
        DB_URL_0 = environment.getProperty("DB_URL_0", String.class);
    }

    /**
     * 获取数据源定义
     *
     * @param dataSourceName 数据源名称
     * @return 数据源定义
     */
    @Override
    public DataSourceDefinition getDataSourceDefinition(String dataSourceName) {
        if ((CRUD.COLUMN_TENANT_ID + "101").equals(dataSourceName)) {
            DataSourceDefinition tenantDataSource = new DataSourceDefinition();
            tenantDataSource.setName(dataSourceName);
            tenantDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            tenantDataSource.setUrl(DB_URL_101);
            tenantDataSource.setUsername(DB_USERNAME_101);
            tenantDataSource.setPassword(DB_PASSWORD_101);
            return tenantDataSource;
        }

        if ((CRUD.COLUMN_TENANT_ID + "0").equals(dataSourceName)) {
            DataSourceDefinition tenantDataSource = new DataSourceDefinition();
            tenantDataSource.setName(dataSourceName);
            tenantDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            tenantDataSource.setUrl(DB_URL_0);
            tenantDataSource.setUsername(DB_USERNAME_101);
            tenantDataSource.setPassword(DB_PASSWORD_101);
            return tenantDataSource;
        }

        return null;
    }

}
