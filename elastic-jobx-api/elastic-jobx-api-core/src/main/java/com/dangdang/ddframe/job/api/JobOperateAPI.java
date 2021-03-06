/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.dangdang.ddframe.job.api;

import com.google.common.base.Optional;

import java.util.Collection;

/**
 * 操作作业的API.
 *
 * @author zhangliang
 */
public interface JobOperateAPI {

    /**
     * 作业立刻执行.
     *
     * <p>作业在不与上次运行中作业冲突的情况下才会启动, 并在启动后自动清理此标记.</p>
     *
     * @param jobName 作业名称
     * @param serverIp 作业服务器IP地址
     */
    void trigger(Optional<String> jobName, Optional<String> serverIp);

    /**
     * 作业暂停.
     * 
     * <p>不会导致重新分片.</p>
     *
     * @param jobName 作业名称
     * @param serverIp 作业服务器IP地址
     */
    void pause(Optional<String> jobName, Optional<String> serverIp);
    
    /**
     * 作业恢复.
     *
     * @param jobName 作业名称
     * @param serverIp 作业服务器IP地址
     */
    void resume(Optional<String> jobName, Optional<String> serverIp);
    
    /**
     * 作业禁用.
     * 
     * <p>会重新分片.</p>
     *
     * @param jobName 作业名称
     * @param serverIp 作业服务器IP地址
     */
    void disable(Optional<String> jobName, Optional<String> serverIp);
    
    /**
     * 作业启用.
     *
     * @param jobName 作业名称
     * @param serverIp 作业服务器IP地址
     */
    void enable(Optional<String> jobName, Optional<String> serverIp);
    
    /**
     * 作业关闭.
     *
     * @param jobName 作业名称
     * @param serverIp 作业服务器IP地址
     */
    void shutdown(Optional<String> jobName, Optional<String> serverIp);
    
    /**
     * 作业删除.
     * 
     * <p>只有停止运行的作业才能删除.</p>
     * 
     * @param jobName 作业名称
     * @param serverIp 作业服务器IP地址
     * @return 因为未停止而导致未能成功删除的作业服务器IP地址列表
     */
    Collection<String> remove(Optional<String> jobName, Optional<String> serverIp);
}
