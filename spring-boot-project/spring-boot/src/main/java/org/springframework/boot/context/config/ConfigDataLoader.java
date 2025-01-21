/*
 * Copyright 2012-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.context.config;

import java.io.IOException;

import org.springframework.boot.BootstrapContext;
import org.springframework.boot.BootstrapRegistry;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.logging.DeferredLogFactory;

/**
 * 用于从指定{@link ConfigDataResource}加载{@link ConfigData}的策略类。
 * 其实现应该添加到{@code META-INF/spring.factories}。其支持如下的构造器参数：
 * <ul>
 *     <li>{@link DeferredLogFactory} 如果加载程序需要延迟日志记录</li>
 *     <li>{@link ConfigurableBootstrapContext} 引导上下文可以用来存储创建成本昂贵、或需要被共享的数据</li>
 *     <li>{@link BootstrapContext}或{@link BootstrapRegistry}</li>
 * </ul>
 *
 * <p>多个加载器不能声明同一个资源
 *
 * @param <R> the resource type
 * @author Phillip Webb
 * @author Madhura Bhave
 * @since 2.4.0
 */
public interface ConfigDataLoader<R extends ConfigDataResource> {

	/**
	 * Returns if the specified resource can be loaded by this instance.
	 * @param context the loader context
	 * @param resource the resource to check.
	 * @return if the resource is supported by this loader
	 */
	default boolean isLoadable(ConfigDataLoaderContext context, R resource) {
		return true;
	}

	/**
	 * Load {@link ConfigData} for the given resource.
	 * @param context the loader context
	 * @param resource the resource to load
	 * @return the loaded config data or {@code null} if the location should be skipped
	 * @throws IOException on IO error
	 * @throws ConfigDataResourceNotFoundException if the resource cannot be found
	 */
	ConfigData load(ConfigDataLoaderContext context, R resource)
			throws IOException, ConfigDataResourceNotFoundException;

}
