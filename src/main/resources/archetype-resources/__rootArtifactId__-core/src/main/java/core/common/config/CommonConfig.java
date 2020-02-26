package ${package}.core.common.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ${package}.core.common.env.EnvConfig;
import tech.ibit.mybatis.SqlBuilder;
import tech.ibit.structlog4j.StructLog4J;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.function.Function;

/**
 * 公共配置
 *
 * @author IBIT TECH
 */
@Configuration
public class CommonConfig {

    // todo 补充"$"
    @Value("{spring.profiles.active:NA}")
    private String activeProfile;

    // todo 补充"$"
    @Value("{spring.application.name:NA}")
    private String appName;

    @Autowired
    private EnvConfig envConfig;

    /**
     * 获取环境变量
     *
     * @return 环境变量
     */
    @Bean
    public EnvConfig envConfig() {
        return EnvConfig.getEnvConfig(activeProfile);
    }

    @PostConstruct
    public void init() {
        // 增加公共字段
        StructLog4J.setGlobalConfig(() -> new Object[]{"env", activeProfile, "service", appName});
        StructLog4J.setTransStackTrace(envConfig.isTransStackTrace());
        SqlBuilder.setValueFormatter(new LinkedHashMap<Class, Function<Object, Object>>() {{
            put(tech.ibit.mybatis.type.CommonEnum.class, o -> ((tech.ibit.mybatis.type.CommonEnum) o).getValue());
        }});
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}