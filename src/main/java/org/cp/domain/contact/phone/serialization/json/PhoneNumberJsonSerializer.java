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
package org.cp.domain.contact.phone.serialization.json;

import org.cp.domain.contact.phone.model.PhoneNumber;
import org.cp.domain.core.serialization.json.AbstractJsonSerializer;
import org.cp.domain.core.serialization.json.JsonSerializer;

/**
 * {@link JsonSerializer} implementation for {@link PhoneNumber}.
 *
 * @author John Blum
 * @see org.cp.domain.contact.phone.model.PhoneNumber
 * @see org.cp.domain.core.serialization.json.AbstractJsonSerializer
 * @see org.cp.domain.core.serialization.json.JsonSerializer
 * @since 0.3.0
 */
public class PhoneNumberJsonSerializer extends AbstractJsonSerializer<PhoneNumber> {

  @Override
  protected Class<PhoneNumber> getType() {
    return PhoneNumber.class;
  }
}
