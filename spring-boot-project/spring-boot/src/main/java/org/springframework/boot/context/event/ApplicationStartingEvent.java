/*
 * Copyright 2012-2021 the original author or authors.
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

package org.springframework.boot.context.event;

import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

/**
 * 事件发布器尽可能早的在{@link SpringApplication}刚启动，监听器必须要在事件发送之前注册，但是{@link Environment}和{@link ApplicationContext}尚未可用时，发出该事件。
 *
 * <p>该事件源时{@link SpringApplication}，但是注意不要在早期阶段过多使用其内部状态，因为它可能在生命周期的后期被修改
 *
 * @author Phillip Webb
 * @author Madhura Bhave
 * @since 1.5.0
 */
@SuppressWarnings("serial")
public class ApplicationStartingEvent extends SpringApplicationEvent {

	/**
	 * SpringApplication
	 */
	private final ConfigurableBootstrapContext bootstrapContext;

	/**
	 * Create a new {@link ApplicationStartingEvent} instance.
	 * @param bootstrapContext the bootstrap context
	 * @param application the current application
	 * @param args the arguments the application is running with
	 */
	public ApplicationStartingEvent(ConfigurableBootstrapContext bootstrapContext, SpringApplication application, String[] args) {
		super(application, args);
		this.bootstrapContext = bootstrapContext;
	}

	/**
	 * Return the bootstrap context.
	 * @return the bootstrap context
	 * @since 2.4.0
	 */
	public ConfigurableBootstrapContext getBootstrapContext() {
		return this.bootstrapContext;
	}

}
