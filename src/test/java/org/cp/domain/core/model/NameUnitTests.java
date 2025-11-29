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
package org.cp.domain.core.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import org.cp.elements.io.IOUtils;
import org.cp.elements.lang.Nameable;
import org.cp.elements.lang.Visitor;

/**
 * Unit Tests for {@link Name}.
 *
 * @author John Blum
 * @see org.junit.jupiter.api.Test
 * @see org.mockito.Mockito
 * @see org.cp.domain.core.model.Name
 * @see org.cp.elements.lang.Nameable
 * @since 0.1.0
 */
class NameUnitTests {

  private void assertName(Name name, String expectedFirstName, String expectedLastName) {
    assertName(name, expectedFirstName, null, expectedLastName);
  }

  private void assertName(Name name, String expectedFirstName, String expectedMiddleName, String expectedLastName) {

    assertThat(name).isNotNull();
    assertThat(name.getName()).isSameAs(name);
    assertThat(name.getFirstName()).isEqualTo(expectedFirstName);
    assertThat(name.getLastName()).isEqualTo(expectedLastName);
    assertThat(name.getMiddleName().orElse(null)).isEqualTo(expectedMiddleName);
  }

  @Test
  void fromName() {

    Name source = Name.of("Jon", "Jason", "Bloom");
    Name target = Name.from(source);

    assertThat(target).isNotNull();
    assertThat(target).isNotSameAs(source);
    assertThat(target).isEqualTo(source);
    assertThat(target.getName()).isSameAs(target);
  }

  @Test
  void fromNameWithMiddleInitial() {

    Name source = Name.of("Jon", "J", "Bloom");
    Name target = Name.from(source);

    assertThat(target).isNotNull();
    assertThat(target).isNotSameAs(source);
    assertThat(target).isEqualTo(source);
    assertThat(target.getMiddleName().orElse(null)).isEqualTo("J");
  }

  @Test
  void fromNameWithNoMiddleName() {

    Name source = Name.of("Jon", "Bloom");
    Name target = Name.from(source);

    assertThat(target).isNotNull();
    assertThat(target).isNotSameAs(source);
    assertThat(target).isEqualTo(source);
    assertThat(target.getFirstName()).isEqualTo("Jon");
    assertThat(target.getMiddleName()).isNotPresent();
    assertThat(target.getLastName()).isEqualTo("Bloom");
  }

  @Test
  void fromNullNameThrowsIllegalArgumentException() {

    assertThatIllegalArgumentException()
      .isThrownBy(() -> Name.from((Name) null))
      .withMessage("Name to copy is required")
      .withNoCause();
  }

  @Test
  @SuppressWarnings("unchecked")
  void fromNameableOfName() {

    Name name = Name.of("Jon", "Jason", "Bloom");

    Nameable<Name> mockNameable = mock(Nameable.class);

    doReturn(name).when(mockNameable).getName();

    Name copy = Name.from(mockNameable);

    assertThat(copy).isNotNull();
    assertThat(copy).isNotSameAs(name);
    assertThat(copy).isEqualTo(name);
    assertName(copy, "Jon", "Jason", "Bloom");

    verify(mockNameable, times(1)).getName();
    verifyNoMoreInteractions(mockNameable);
  }

  @Test
  @SuppressWarnings("unchecked")
  void fromNameableOfNameWithMiddleInitial() {

    Name name = Name.of("Jon", "J", "Bloom");

    Nameable<Name> mockNameable = mock(Nameable.class);

    doReturn(name).when(mockNameable).getName();

    Name copy = Name.from(mockNameable);

    assertThat(copy).isNotNull();
    assertThat(copy).isNotSameAs(name);
    assertThat(copy).isEqualTo(name);
    assertName(copy, "Jon", "J", "Bloom");

    verify(mockNameable, times(1)).getName();
    verifyNoMoreInteractions(mockNameable);
  }

  @Test
  @SuppressWarnings("unchecked")
  void fromNameableOfNameWithNoMiddleName() {

    Name name = Name.of("Jon", "Bloom");

    Nameable<Name> mockNameable = mock(Nameable.class);

    doReturn(name).when(mockNameable).getName();

    Name copy = Name.from(mockNameable);

    assertThat(copy).isNotNull();
    assertThat(copy).isNotSameAs(name);
    assertThat(copy).isEqualTo(name);
    assertName(copy, "Jon", "Bloom");

    verify(mockNameable, times(1)).getName();
    verifyNoMoreInteractions(mockNameable);
  }

  @Test
  void fromNullNameableThrowsIllegalArgumentException() {

    assertThatIllegalArgumentException()
      .isThrownBy(() -> Name.from((Nameable<Name>) null))
      .withMessage("Nameable of Name is required")
      .withNoCause();
  }

  @Test
  void ofFirstNameAndLastName() {
    assertName(Name.of("Jon", "Bloom"), "Jon", "Bloom");
  }

  @Test
  void ofFirstNameMiddleNameAndLastName() {
    assertName(Name.of("Jon", "Jason", "Bloom"),
      "Jon", "Jason", "Bloom");
  }

  @Test
  void parseStringContainingFirstNameAndLastName() {
    assertName(Name.parse("Jon Bloom"), "Jon", "Bloom");
  }

  @Test
  void parseStringContainingFirstNameMiddleInitialAndLastName() {
    assertName(Name.parse("Jon J Bloom"), "Jon", "J", "Bloom");
  }

  @Test
  void parseStringContainingFirstNameMiddleNameAndLastName() {
    assertName(Name.parse("Jon Jason Bloom"), "Jon", "Jason", "Bloom");
  }

  @Test
  void parseStringContainingFirstNameLastNameAndSuffix() {
    assertName(Name.parse("Jon Bloom Sr"), "Jon", "Bloom");
  }

  @Test
  void parseStringContainingFirstNameMiddleInitialLastNameAndSuffix() {
    assertName(Name.parse("Jon J Bloom Jr."), "Jon", "J", "Bloom");
  }

  @Test
  void parseStringContainingFirstNameMiddleNameLastNameAndUnknownSuffix() {
    assertName(Name.parse("Charles Gordon Howell III"),
      "Charles", "Gordon", "Howell");
  }

  @Test
  void parseStringContainingMissTitleFirstNameAndLastName() {
    assertName(Name.parse("Miss Ellie Bloom"), "Ellie", "Bloom");
  }

  @Test
  void parseStringContainingMissesTitleFirstNameMiddleInitialAndLastName() {
    assertName(Name.parse("Mrs. Sarah E Bloom"), "Sarah", "E", "Bloom");
  }

  @Test
  void parseStringContainingMisterTitleFirstNameMiddleNameAndLastName() {
    assertName(Name.parse("Mr. Jon Jason Bloom"), "Jon", "Jason", "Bloom");
  }

  @Test
  void parseStringContainingMultipleTitlesWithNameAndMultipleSuffixes() {
    assertName(Name.parse("Sir Dr. Senior Bloom Jr. 111"), "Senior", "Bloom");
  }

  @Test
  void parseStringContainingMultipleTitlesWithFullNameAndMultipleSuffixes() {
    assertName(Name.parse("Sir Dr. Senior Xander Bloom Jr. 11"),
      "Senior", "Xander", "Bloom");
  }

  @Test
  void parseStringContainingPaddedFirstNameMiddleNameAndLastName() {
    assertName(Name.parse("  Jon   J Bloom    "), "Jon", "J", "Bloom");
  }

  @Test
  void parseStringContainingTitleFirstNameLastNameAndSuffix() {
    assertName(Name.parse("Dr. Evil Mister Sr."), "Evil", "Mister");
  }

  @Test
  void parseStringContainingInvalidNamesThrowsIllegalArgumentException() {

    Arrays.asList("Jon", "Jon Jr.", "Mr. Jon", "", "  ", null).forEach(name ->
      assertThatIllegalArgumentException()
        .isThrownBy(() -> Name.parse(name))
        .withMessage("First and last name are required; was [%s]", name)
        .withNoCause());
  }

  @Test
  void constructName() {
    assertName(new Name("Jon", "Jason", "Bloom"),
      "Jon", "Jason", "Bloom");
  }

  @Test
  void constructNameWithInvalidMiddleNames() {

    Arrays.asList("", "  ", null).forEach(middleName ->
      assertName(new Name("Jon", middleName, "Doe"), "Jon", "Doe"));
  }

  @Test
  void constructNameWithNoFirstName() {

    assertThatIllegalArgumentException()
      .isThrownBy(() -> new Name(" ", "J", "Bloom"))
      .withMessage("First name is required")
      .withNoCause();
  }

  @Test
  void constructNameWithNoLastName() {

    assertThatIllegalArgumentException()
      .isThrownBy(() -> Name.of("Jon", " ", ""))
      .withMessage("Last name is required")
      .withNoCause();
  }

  @Test
  void acceptsVisitor() {

    Name name = Name.of("Jon", "Bloom");

    Visitor mockVisitor = mock(Visitor.class);

    assertThat(name).isNotNull();

    name.accept(mockVisitor);

    verify(mockVisitor, times(1)).visit(eq(name));
    verifyNoMoreInteractions(mockVisitor);
  }

  @Test
  void changeLastName() {

    Name name = Name.of("Jon", "Bloom");

    assertName(name, "Jon", "Bloom");

    Name nameChange = name.change("Doe");

    assertThat(nameChange).isNotSameAs(name);
    assertName(nameChange, "Jon", "Doe");
  }

  @Test
  void likeByFirstNameReturnsTrue() {

    Name jonBloom = Name.of("Jon", "R", "Bloom");
    Name jonBlum = Name.of("Jon", "J", "Blum");

    assertThat(jonBloom.like(jonBlum)).isTrue();
    assertThat(jonBlum.like(jonBloom)).isTrue();
  }

  @Test
  void likeByLastNameReturnsTrue() {

    Name jonBloom = Name.of("Jon", "R", "Bloom");
    Name johnBloom = Name.of("John", "J", "Bloom");

    assertThat(jonBloom.like(johnBloom)).isTrue();
    assertThat(johnBloom.like(jonBloom)).isTrue();
  }

  @Test
  void likeByUnlikeNamesReturnsFalse() {

    Name johnBlum = Name.of("John", "J", "Blum");
    Name jonBloom = Name.of("Jon", "J", "Bloom");

    assertThat(johnBlum.like(jonBloom)).isFalse();
    assertThat(jonBloom.like(johnBlum)).isFalse();
  }

  @Test
  void likeIsNullSafe() {
    assertThat(Name.of("Jon", "R", "Bloom").like(null)).isFalse();
  }

  @Test
  void cloneIsSuccessful() {

    Name name = Name.of("Jon", "X", "Bloom");
    Name clone = (Name) name.clone();

    assertThat(clone).isNotNull();
    assertThat(clone).isNotSameAs(name);
    assertThat(clone).isEqualTo(name);
    assertName(clone, "Jon", "X", "Bloom");
  }

  @Test
  @SuppressWarnings("all")
  void compareToItselfIsIdentical() {

    Name name = Name.of("Jon", "J", "Bloom");

    assertThat(name).isNotNull();
    assertThat(name.compareTo(name)).isZero();
  }

  @Test
  void compareToWithEqualNamesIsEqual() {

    Name jonBloomOne = Name.of("Jon", "Bloom");
    Name jonBloomTwo = Name.of("Jon", "Bloom");

    assertThat(jonBloomOne).isNotNull();
    assertThat(jonBloomTwo).isNotNull();
    assertThat(jonBloomOne).isNotSameAs(jonBloomTwo);
    assertThat(jonBloomOne.compareTo(jonBloomTwo)).isZero();
  }

  @Test
  void compareToIsGreaterThan() {

    Name jonBloom = Name.of("Jon", "Bloom");
    Name sarahBloom = Name.of("Sarah", "Bloom");

    assertThat(jonBloom).isNotNull();
    assertThat(sarahBloom).isNotNull();
    assertThat(sarahBloom).isNotSameAs(jonBloom);
    assertThat(sarahBloom.compareTo(jonBloom)).isGreaterThan(0);
  }

  @Test
  void compareToIsLessThan() {

    Name ellieBloom = Name.of("Ellie", "Bloom");
    Name jonBloom = Name.of("Jon", "Bloom");

    assertThat(ellieBloom).isNotNull();
    assertThat(jonBloom).isNotNull();
    assertThat(ellieBloom).isNotSameAs(jonBloom);
    assertThat(ellieBloom.compareTo(jonBloom)).isLessThan(0);
  }

  @Test
  void equalsWithEqualNamesReturnsTrue() {

    Name jonBloomOne = Name.of("Jon", "J", "Bloom");
    Name jonBloomTwo = Name.of("Jon", "J", "Bloom");

    assertThat(jonBloomOne).isNotNull();
    assertThat(jonBloomTwo).isNotNull();
    assertThat(jonBloomOne).isNotSameAs(jonBloomTwo);
    assertThat(jonBloomOne.equals(jonBloomTwo)).isTrue();
  }

  @Test
  void equalsWithEffectivelyEqualNamesReturnsTrue() {

    Name jonBloomOne = Name.of("Jon", "Bloom");
    Name jonBloomTwo = Name.of("Jon", "Bloom");

    assertThat(jonBloomOne).isNotNull();
    assertThat(jonBloomTwo).isNotNull();
    assertThat(jonBloomOne).isNotSameAs(jonBloomTwo);
    assertThat(jonBloomOne.equals(jonBloomTwo)).isTrue();
  }

  @Test
  @SuppressWarnings("all")
  void equalsWithIdenticalNamesReturnsTrue() {

    Name name = Name.of("Jon", "J", "Bloom");

    assertThat(name).isNotNull();
    assertThat(name.equals(name)).isTrue();
  }

  @Test
  void equalsWithNearlyEqualNamesReturnsFalse() {

    Name jonBloomOne = Name.of("Jon", "J", "Bloom");
    Name jonBloomTwo = Name.of("Jon", "Bloom");

    assertThat(jonBloomOne).isNotNull();
    assertThat(jonBloomTwo).isNotNull();
    assertThat(jonBloomOne).isNotSameAs(jonBloomTwo);
    assertThat(jonBloomOne.equals(jonBloomTwo)).isFalse();
  }

  @Test
  void equalsWithSimilarNamesReturnsFalse() {

    Name johnBlum = Name.of("John", "J", "Blum");
    Name jonBloom = Name.of("Jon", "J", "Bloom");

    assertThat(johnBlum).isNotNull();
    assertThat(jonBloom).isNotNull();
    assertThat(johnBlum).isNotSameAs(jonBloom);
    assertThat(johnBlum.equals(jonBloom)).isFalse();
  }

  @Test
  @SuppressWarnings("all")
  void equalsNullIsNullSafeReturnsFalse() {
    assertThat(Name.of("Jon", "J", "Bloom").equals(null)).isFalse();
  }

  @Test
  @SuppressWarnings("all")
  void equalsObjectIsFalse() {
    assertThat(Name.of("Jon", "Bloom").equals("Jon Bloom")).isFalse();
  }

  @Test
  void hashCodeForNameIsNotZero() {
    assertThat(Name.of("Jon", "J", "Bloom").hashCode()).isNotEqualTo(0);
  }

  @Test
  void hashCodeForDifferentNamesAreNotEqual() {
    assertThat(Name.of("Jon", "J", "Bloom").hashCode())
      .isNotEqualTo(Name.of("John", "J", "Blum").hashCode());
  }

  @Test
  void hashCodeForEqualNamesAreEqual() {
    assertThat(Name.of("John", "Blum")).hasSameHashCodeAs(Name.of("John", "Blum"));
  }

  @Test
  void hashCodeForIdenticalNamesAreEqual() {

    Name name = Name.of("John", "J", "Blum");

    assertThat(name).isNotNull();
    assertThat(name).hasSameHashCodeAs(name);
  }

  @Test
  void hashCodeForSimilarNamesAreNotEqual() {
    assertThat(Name.of("Jon", "J", "Bloom").hashCode())
      .isNotEqualTo(Name.of("Jon", "Bloom").hashCode());
  }

  @Test
  void toStringWithFirstNameAndLastNameIsCorrect() {
    assertThat(Name.of("Jon", "Bloom").toString()).isEqualTo("Jon Bloom");
  }

  @Test
  void toStringWithFirstNameMiddleNameAndLastNameIsCorrect() {

    assertThat(Name.of("Jon", "J", "Bloom").toString()).isEqualTo("Jon J Bloom");
    assertThat(Name.of("Jon", "Jason", "Bloom").toString()).isEqualTo("Jon Jason Bloom");
  }

  @Test
  void toStringWithTitleFirstNameMiddleNameLastNameAndSuffixIsStringWithFirstNameMiddleNameAndLastNameOnly() {
    assertThat(Name.parse("Dr. Evil C Doer Sr.").toString()).isEqualTo("Evil C Doer");
  }

  @Test
  void nameIsSerializable() throws IOException, ClassNotFoundException {

    Name name = Name.of("Jon", "J", "Bloom");

    assertThat(name).isNotNull();

    byte[] nameBytes = IOUtils.serialize(name);

    assertThat(nameBytes).isNotNull();
    assertThat(nameBytes).isNotEmpty();

    Name deserializedName = IOUtils.deserialize(nameBytes);

    assertName(deserializedName, "Jon", "J", "Bloom");
    assertThat(deserializedName).isNotSameAs(name);
  }
}
