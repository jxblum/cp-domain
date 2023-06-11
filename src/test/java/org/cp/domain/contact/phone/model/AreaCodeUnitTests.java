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
package org.cp.domain.contact.phone.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import org.cp.elements.io.IOUtils;
import org.cp.elements.lang.Renderer;
import org.testcontainers.shaded.org.apache.commons.lang3.StringUtils;

/**
 * Unit Tests for {@link AreaCode}.
 *
 * @author John Blum
 * @see org.junit.jupiter.api.Test
 * @see org.mockito.Mockito
 * @see org.cp.domain.contact.phone.model.AreaCode
 * @since 0.1.0
 */
public class AreaCodeUnitTests {

  @Test
  public void fromAreaCode() {

    AreaCode mockAreaCode = mock(AreaCode.class);

    doReturn("123").when(mockAreaCode).getNumber();

    AreaCode copy = AreaCode.from(mockAreaCode);

    assertThat(copy).isNotNull();
    assertThat(copy.getNumber()).isEqualTo("123");

    verify(mockAreaCode, times(1)).getNumber();
    verifyNoMoreInteractions(mockAreaCode);
  }

  @Test
  public void fromNullAreaCode() {

    assertThatIllegalArgumentException()
      .isThrownBy(() -> AreaCode.from(null))
      .withMessage("AreaCode to copy is required")
      .withNoCause();
  }

  @Test
  public void ofNumber() {

    AreaCode areaCode = AreaCode.of("987");

    assertThat(areaCode).isNotNull();
    assertThat(areaCode.getNumber()).isEqualTo("987");
  }

  @Test
  public void parseTenDigitPhoneNumber() {

    AreaCode areaCode = AreaCode.parse("5035551234");

    assertThat(areaCode).isNotNull();
    assertThat(areaCode.getNumber()).isEqualTo("503");
  }

  @Test
  public void parseFormattedTenDigitPhoneNumber() {

    AreaCode areaCode = AreaCode.parse("(971) 555-2480");

    assertThat(areaCode).isNotNull();
    assertThat(areaCode.getNumber()).isEqualTo("971");

    areaCode = AreaCode.parse("563-555-1234");

    assertThat(areaCode).isNotNull();
    assertThat(areaCode.getNumber()).isEqualTo("563");
  }

  @Test
  public void parsePhoneNumberWithNoAreaCode() {

    assertThatIllegalArgumentException()
      .isThrownBy(() -> AreaCode.parse("555-1234"))
      .withMessage("Phone Number [555-1234] to parse the Area Code from must be 10-digits")
      .withNoCause();
  }

  @Test
  public void parseInvalidPhoneNumber() {

    Arrays.asList("  ", "", null).forEach(phoneNumber ->
      assertThatIllegalArgumentException()
        .isThrownBy(() -> AreaCode.parse(phoneNumber))
        .withMessage("Phone Number [%s] to parse the Area Code from must be 10-digits", phoneNumber)
        .withNoCause());
  }

  @Test
  public void constructAreaCode() {

    AreaCode areaCode = new AreaCode("248");

    assertThat(areaCode).isNotNull();
    assertThat(areaCode.getNumber()).isEqualTo("248");
  }

  @Test
  public void constructAreaCodeWithTooFewDigits() {

    assertThatIllegalArgumentException()
      .isThrownBy(() -> new AreaCode("12"))
      .withMessage("AreaCode [12] must be a %s-digit number", AreaCode.REQUIRED_AREA_CODE_LENGTH)
      .withNoCause();
  }

  @Test
  public void constructAreaCodeWithTooManyDigits() {

    assertThatIllegalArgumentException()
      .isThrownBy(() -> new AreaCode("1234"))
      .withMessage("AreaCode [1234] must be a %s-digit number", AreaCode.REQUIRED_AREA_CODE_LENGTH)
      .withNoCause();
  }

  @Test
  public void cloneIsCorrect() throws CloneNotSupportedException {

    AreaCode areaCode = AreaCode.of("123");

    AreaCode clone = (AreaCode) areaCode.clone();

    assertThat(clone).isNotNull();
    assertThat(clone).isNotSameAs(areaCode);
    assertThat(clone).isEqualTo(areaCode);
  }

  @Test
  @SuppressWarnings("all")
  public void compareToIsCorrect() {

    AreaCode areaCodeOne = AreaCode.of("123");
    AreaCode areaCodeTwo = AreaCode.of("248");

    assertThat(areaCodeOne.compareTo(areaCodeOne)).isZero();
    assertThat(areaCodeOne.compareTo(areaCodeTwo)).isLessThan(0);
    assertThat(areaCodeTwo.compareTo(areaCodeOne)).isGreaterThan(0);
  }

  @Test
  @SuppressWarnings("all")
  public void equalsIsCorrect() {

    AreaCode areaCodeOne = AreaCode.of("123");
    AreaCode areaCodeTwo = AreaCode.of("248");

    assertThat(areaCodeOne).isEqualTo(areaCodeOne);
    assertThat(areaCodeOne).isEqualTo(AreaCode.of("123"));
    assertThat(areaCodeOne).isNotEqualTo(areaCodeTwo);
    assertThat(areaCodeOne).isNotEqualTo("123");
    assertThat(areaCodeOne).isNotEqualTo("test");
  }

  @Test
  public void hashCodeIsCorrect() {

    AreaCode areaCodeOne = AreaCode.of("123");
    AreaCode areaCodeTwo = AreaCode.of("248");

    assertThat(areaCodeOne).hasSameHashCodeAs(areaCodeOne);
    assertThat(areaCodeOne).hasSameHashCodeAs(AreaCode.of("123"));
    assertThat(areaCodeOne).doesNotHaveSameHashCodeAs(areaCodeTwo);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void rendersCorrectly() {

    Renderer<AreaCode> mockRenderer = mock(Renderer.class);

    doAnswer(invocation -> StringUtils.reverse(invocation.getArgument(0, AreaCode.class).getNumber()))
      .when(mockRenderer).render(any());

    AreaCode areaCode = AreaCode.of("123");

    assertThat(areaCode).isNotNull();
    assertThat(areaCode.render(mockRenderer)).isEqualTo("321");

    verify(mockRenderer, times(1)).render(eq(areaCode));
    verifyNoMoreInteractions(mockRenderer);
  }
  @Test
  public void serializationIsCorrect() throws IOException, ClassNotFoundException {

    AreaCode areaCode = AreaCode.of("123");

    byte[] areaCodeBytes = IOUtils.serialize(areaCode);

    assertThat(areaCodeBytes).isNotNull();
    assertThat(areaCodeBytes).isNotEmpty();

    AreaCode deserializedAreaCode = IOUtils.deserialize(areaCodeBytes);

    assertThat(deserializedAreaCode).isNotNull();
    assertThat(deserializedAreaCode).isNotSameAs(areaCode);
    assertThat(deserializedAreaCode).isEqualTo(areaCode);
  }

  @Test
  public void toStringIsCorrect() {

    AreaCode areaCode = AreaCode.of("123");

    assertThat(areaCode).isNotNull();
    assertThat(areaCode.toString()).isEqualTo("123");
  }
}