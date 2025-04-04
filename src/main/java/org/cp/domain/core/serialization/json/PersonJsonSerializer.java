/*
 * Copyright 2017-Present Author or Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cp.domain.core.serialization.json;

import org.cp.domain.core.model.Person;
import org.cp.elements.data.serialization.Serializer;

/**
 * {@literal JSON} {@link Serializer} implementation for {@link Person}.
 *
 * @author John Blum
 * @see org.cp.domain.core.serialization.json.AbstractJsonSerializer
 * @since 0.3.0
 */
public class PersonJsonSerializer extends AbstractJsonSerializer<Person> {

  @Override
  protected Class<Person> getType() {
    return Person.class;
  }
}
