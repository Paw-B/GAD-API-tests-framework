package config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({ "classpath:config.properties" })
public interface TestConfig extends Config {

	@Key("baseUrl")
	String baseUrl();
}