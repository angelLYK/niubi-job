/*
 * Copyright 2002-2016 the original author or authors.
 *
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
 */

package com.zuoxiaolong.niubi.job.api.data;

import com.zuoxiaolong.niubi.job.core.exception.UnknownGenericTypeException;
import com.zuoxiaolong.niubi.job.core.helper.JsonHelper;
import lombok.Getter;
import lombok.Setter;
import org.apache.curator.framework.recipes.cache.ChildData;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Xiaolong Zuo
 * @since 16/1/13 23:23
 */
@Setter
@Getter
public class GenericData<T> extends BaseData {

    private T data;

    public GenericData(ChildData childData) {
        this(childData.getPath(), childData.getData());
    }

    public GenericData(String path, byte[] bytes) {
        this.path = path;
        this.id = this.path.substring(this.path.lastIndexOf("/") + 1);
        this.data = JsonHelper.fromJson(bytes, getGenericType());
    }

    public GenericData(String path, T data) {
        this.path = path;
        this.id = this.path.substring(this.path.lastIndexOf("/") + 1);
        this.data = data;
    }

    private Class<T> getGenericType() {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return (Class<T>) parameterizedType.getActualTypeArguments()[0];
        }
        throw new UnknownGenericTypeException();
    }

    public byte[] getDataBytes() {
        return JsonHelper.toBytes(data);
    }

    @Override
    public String toString() {
        return "GenericData{" +
                "data=" + JsonHelper.toJson(data) +
                '}';
    }
}