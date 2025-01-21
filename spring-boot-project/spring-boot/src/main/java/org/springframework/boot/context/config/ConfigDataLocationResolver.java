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

import java.util.Collections;
import java.util.List;

import org.springframework.boot.BootstrapContext;
import org.springframework.boot.BootstrapRegistry;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.logging.DeferredLogFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

/**
 * 用于解决{@link ConfigDataLocation}进入到{@link ConfigDataResource}的策略接口。
 * 实现类应该加入到{@code META-INF/spring.factories}。其支持如下的构造器参数：
 * <ul>
 *     <li>{@link DeferredLogFactory} 如果加载程序需要延迟日志记录</li>
 *     <li>{@link Binder} 解决器需要从初始化的{@link Environment}中发现值</li>
 *     <li>{@link ResourceLoader} 解决器需要资源加载</li>
 *     <li>{@link ConfigurableBootstrapContext}  引导上下文可以用来存储创建成本昂贵、或需要被共享的数据</li>
 *     <li>{@link BootstrapContext}或{@link BootstrapRegistry} </li>
 * </ul>
 *
 * <p>解决器实现可以使用{@link Ordered}或{@link Order}注解。支持给定路径的第一个解决器将被使用。
 *
 * @param <R> the location type
 * @author Phillip Webb
 * @author Madhura Bhave
 * @since 2.4.0
 */
public interface ConfigDataLocationResolver<R extends ConfigDataResource> {

	/**
	 * 返回特定路径是否可以被解决器解决
	 *
	 * @param context the location resolver context
	 * @param location the location to check.
	 * @return if the location is supported by this resolver
	 */
	boolean isResolvable(ConfigDataLocationResolverContext context, ConfigDataLocation location);

	/**
	 * 将一个{@link ConfigDataLocation}解决为一个或多个{@link ConfigDataResource}实例
	 *
	 * @param context the location resolver context
	 * @param location the location that should be resolved
	 * @return a list of {@link ConfigDataResource resources} in ascending priority order.
	 * @throws ConfigDataLocationNotFoundException on a non-optional location that cannot
	 * be found
	 * @throws ConfigDataResourceNotFoundException if a resolved resource cannot be found
	 */
	List<R> resolve(ConfigDataLocationResolverContext context, ConfigDataLocation location) throws ConfigDataLocationNotFoundException, ConfigDataResourceNotFoundException;

	/**
	 * 将一个{@link ConfigDataLocation}解决为基于指定profile的一个或多个{@link ConfigDataResource}。
	 * 一旦从贡献值中推断出配置文件，就会调用此方法。默认情况下，此方法返回一个空列表。
	 *
	 * @param context the location resolver context
	 * @param location the location that should be resolved
	 * @param profiles profile information
	 * @return a list of resolved locations in ascending priority order.
	 * @throws ConfigDataLocationNotFoundException on a non-optional location that cannot
	 * be found
	 */
	default List<R> resolveProfileSpecific(ConfigDataLocationResolverContext context, ConfigDataLocation location, Profiles profiles) throws ConfigDataLocationNotFoundException {
		return Collections.emptyList();
	}

}
