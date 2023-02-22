/*
 * Copyright 2011-Present Author or Authors.
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
package org.cp.domain.geo.model.usa.cities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import org.cp.domain.geo.enums.Country;
import org.cp.domain.geo.enums.State;

/**
 * Unit Tests for {@link TrentonNewJersey}.
 *
 * @author John Blum
 * @see org.junit.Test
 * @see org.cp.domain.geo.model.usa.cities.TrentonNewJersey
 * @since 0.1.0
 */
public class TrentonNewJerseyUnitTests {

  @Test
  public void trentonNewJerseyPropertiesAreCorrect() {

    assertThat(TrentonNewJersey.INSTANCE.getName()).isEqualTo("Trenton");
    assertThat(TrentonNewJersey.INSTANCE.getCountry().orElse(null)).isEqualTo(Country.UNITED_STATES_OF_AMERICA);
    assertThat(TrentonNewJersey.INSTANCE.getState()).isEqualTo(State.NEW_JERSEY);
    assertThat(TrentonNewJersey.INSTANCE.isCapital()).isTrue();
  }
}