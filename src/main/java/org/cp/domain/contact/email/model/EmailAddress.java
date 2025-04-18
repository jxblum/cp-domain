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
package org.cp.domain.contact.email.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.cp.domain.core.serialization.json.JsonSerializable;
import org.cp.elements.lang.Assert;
import org.cp.elements.lang.Nameable;
import org.cp.elements.lang.ObjectUtils;
import org.cp.elements.lang.StringUtils;
import org.cp.elements.lang.Visitable;
import org.cp.elements.lang.annotation.Alias;
import org.cp.elements.lang.annotation.Immutable;
import org.cp.elements.lang.annotation.NotNull;
import org.cp.elements.lang.annotation.Nullable;
import org.cp.elements.lang.annotation.ThreadSafe;
import org.cp.elements.security.model.User;
import org.cp.elements.util.ComparatorResultBuilder;

/**
 * Abstract Data Type (ADT) modeling an {@literal email address}.
 *
 * @author John Blum
 * @see java.lang.Cloneable
 * @see java.lang.Comparable
 * @see java.io.Serializable
 * @see org.cp.domain.core.serialization.json.JsonSerializable
 * @see org.cp.elements.lang.Visitable
 * @see org.cp.elements.lang.annotation.Immutable
 * @see org.cp.elements.lang.annotation.ThreadSafe
 * @see org.cp.elements.security.model.User
 * @since 0.1.0
 */
@Immutable
@ThreadSafe
@JsonIgnoreProperties({ "domainName", "username" })
public class EmailAddress implements Cloneable, Comparable<EmailAddress>, JsonSerializable, Serializable, Visitable {

  private static final String EMAIL_ADDRESS_AT_SYMBOL = "@";
  private static final String EMAIL_ADDRESS_TO_STRING = "%1$s".concat(EMAIL_ADDRESS_AT_SYMBOL).concat("%2$s");

  /**
   * Factory method used to construct a new {@link EmailAddress} copied from the given {@link EmailAddress}.
   *
   * @param emailAddress {@link EmailAddress} to copy; must not be {@literal null}.
   * @return a new {@link EmailAddress} copied from the existing, required {@link EmailAddress}.
   * @throws IllegalArgumentException if the given {@link EmailAddress} to copy is {@literal null}.
   */
  public static @NotNull EmailAddress from(@NotNull EmailAddress emailAddress) {
    Assert.notNull(emailAddress, "Email Address to copy is required");
    return new EmailAddress(emailAddress.getUser(), emailAddress.getDomain());
  }

  /**
   * Factory method used to construct a new {@link EmailAddress} initialized with the given {@link User}
   * and {@link Domain}.
   *
   * @param user {@link User} containing the {@link String username} used as the {@literal handle}
   * or {@literal local part} of the new {@link EmailAddress}; must not be {@literal null}.
   * @param domain {@link Domain} of the new {@link EmailAddress}; must not be {@literal null}.
   * @return a new {@link EmailAddress} initialized with the given, required {@link User} and {@link Domain}.
   * @throws IllegalArgumentException if the given {@link User} or {@link Domain} are {@literal null}.
   * @see org.cp.domain.contact.email.model.EmailAddress.Domain
   * @see org.cp.elements.security.model.User
   */
  @JsonCreator
  public static @NotNull EmailAddress of(@NotNull @JsonProperty("user") User<String> user,
      @NotNull @JsonProperty("domain") Domain domain) {

    return new EmailAddress(user, domain);
  }

  /**
   * Factory method used to construct a new {@link EmailAddress} parsed from the given {@link String}
   * representing an {@literal email address}.
   * <p>
   * {@link String Email Addresses} are expected to be in the format {@link String jonDoe@example.com}.
   *
   * @param emailAddress {@link String} containing the {@literal email address} to parse;
   * must not be {@literal null} or {@literal empty}; must be a valid {@literal email address}.
   * @return a new {@link EmailAddress} parsed from the given, required {@link String}
   * representing an {@link EmailAddress}.
   * @throws IllegalArgumentException if the given {@link String email address} to parse is {@literal null}
   * or {@literal empty}, or if the given {@link String email address} format is not valid.
   */
  public static @NotNull EmailAddress parse(@NotNull String emailAddress) {

    int indexOfAtSymbol = assertEmailAddress(emailAddress);

    String username = emailAddress.substring(0, indexOfAtSymbol);
    String domainName = emailAddress.substring(indexOfAtSymbol + 1);

    return new EmailAddress(User.named(username), Domain.parse(domainName));
  }

  private static int assertEmailAddress(String emailAddress) {

    Assert.hasText(emailAddress, "Email Address [%s] to parse is required", emailAddress);

    int indexOfAtSymbol = emailAddress.indexOf(EMAIL_ADDRESS_AT_SYMBOL);

    Assert.isTrue(indexOfAtSymbol > 0, "Email Address [%s] format is not valid", emailAddress);

    return indexOfAtSymbol;
  }

  private final Domain domain;

  private final User<String> user;

  /**
   * Constructs a new {@link EmailAddress} initialized with the given, required {@link User} and {@link Domain}.
   *
   * @param user {@link User} containing the {@link String username} used as the {@literal handle}
   * or {@literal local part} for this {@link EmailAddress}; must not be {@literal null}.
   * @param domain {@link Domain} containing the {@link String domain name} of this {@link EmailAddress};
   * must not be {@literal null}.
   * @throws IllegalArgumentException if the given {@link User} or {@link Domain} are {@literal null}.
   * @see org.cp.domain.contact.email.model.EmailAddress.Domain
   * @see org.cp.elements.security.model.User
   */
  public EmailAddress(@NotNull User<String> user, @NotNull Domain domain) {

    this.user = ObjectUtils.requireObject(user, "User is required");
    this.domain = ObjectUtils.requireObject(domain, "Domain is required");
  }

  /**
   * Gets the {@link Domain} of this {@link EmailAddress}.
   *
   * @return the {@link Domain} of this {@link EmailAddress}.
   * @see Domain
   */
  public @NotNull Domain getDomain() {
    return this.domain;
  }

  /**
   * Gets the {@link String domain name} of this {@link EmailAddress}.
   *
   * @return the {@link String domain name} of this {@link EmailAddress}.
   * @see Domain#toString()
   * @see #getDomain()
   */
  public @NotNull String getDomainName() {
    return getDomain().toString();
  }

  /**
   * Gets the {@link User} of this {@link EmailAddress}.
   *
   * @return the {@link User} of this {@link EmailAddress}.
   * @see org.cp.elements.security.model.User
   */
  public @NotNull User<String> getUser() {
    return this.user;
  }

  /**
   * Gets the {@link String username}, {@link String handle} or {@link String local part} of this {@link EmailAddress}.
   *
   * @return the {@link String username}, {@link String handle} or {@link String local part}
   * of this {@link EmailAddress}.
   * @see org.cp.elements.security.model.User#getName()
   * @see #getUser()
   */
  public @NotNull String getUsername() {
    return getUser().getName();
  }

  @Override
  @SuppressWarnings("all")
  @Alias(forMember = "from")
  protected Object clone() throws CloneNotSupportedException {
    return from(this);
  }

  @Override
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public int compareTo(@NotNull EmailAddress that) {

    return ComparatorResultBuilder.<Comparable>create()
      .doCompare(this.getDomain(), that.getDomain())
      .doCompare(this.getUsername(), that.getUsername())
      .build();
  }

  @Override
  public boolean equals(@Nullable Object obj) {

    if (this == obj) {
      return true;
    }

    if (!(obj instanceof EmailAddress that)) {
      return false;
    }

    return ObjectUtils.equals(this.getUsername(), that.getUsername())
      && ObjectUtils.equals(this.getDomain(), that.getDomain());
  }

  @Override
  public int hashCode() {
    return ObjectUtils.hashCodeOf(getUsername(), getDomain());
  }

  @Override
  public @NotNull String toString() {
    return EMAIL_ADDRESS_TO_STRING.formatted(getUsername(), getDomainName());
  }

  /**
   * Abstract Data Type (ADT) modeling the {@literal domain} of an {@link EmailAddress}.
   *
   * @author John Blum
   * @see java.lang.Cloneable
   * @see java.lang.Comparable
   * @see java.io.Serializable
   * @see org.cp.elements.lang.Nameable
   * @see org.cp.elements.lang.annotation.Immutable
   * @see org.cp.elements.lang.annotation.ThreadSafe
   * @since 0.1.0
   */
  @Immutable
  @ThreadSafe
  @JsonIgnoreProperties("extensionName")
  public static class Domain implements Cloneable, Comparable<Domain>, Nameable<String>, Serializable {

    private static final String DOMAIN_DOT_SEPARATOR = StringUtils.DOT_SEPARATOR;
    private static final String DOMAIN_TO_STRING = "%1$s".concat(DOMAIN_DOT_SEPARATOR).concat("%2$s");

    /**
     * Factory method used to construct a new {@link Domain} copied from the given {@link Domain}.
     *
     * @param domain {@link Domain} to copy; must not be {@literal null}.
     * @return a new {@link Domain} copied from the existing, required {@link Domain}.
     * @throws IllegalArgumentException if the given {@link Domain} to copy is {@literal null}.
     */
    public static @NotNull Domain from(@NotNull Domain domain) {
      Assert.notNull(domain, "Domain to copy is required");
      return new Domain(domain.getName(), domain.getExtension());
    }

    /**
     * Factory method used to construct a new {@link Domain} initialized with the given {@link String name}
     * and {@link Extensions}.
     *
     * @param name {@link String name} of this {@link Domain}; must not be {@literal null} or {@literal empty}.
     * @param extension {@link Extension} of this {@link Domain}.
     * @return a new {@link Domain} initialized with the given, required {@link String name} and {@link Extension}.
     * @throws IllegalArgumentException if the given {@link String name} is {@literal null} or {@literal empty},
     * or {@link Extension} is {@literal null}.
     * @see org.cp.domain.contact.email.model.EmailAddress.Domain.Extension
     */
    @JsonCreator
    public static @NotNull Domain of(@NotNull @JsonProperty("name") String name,
        @NotNull @JsonProperty("extension") Extension extension) {

      return new Domain(name, extension);
    }

    /**
     * Factory method used to construct a new {@link Domain} initialized with the given {@link String name}
     * and {@link String domain extension}.
     *
     * @param name {@link String name} of the new {@link Domain}; must not be {@literal null} or {@literal empty}.
     * @param extensionName {@link String extension name} of the new {@link Domain};
     * must not be {@literal null} or {@literal empty}.
     * @return a new {@link Domain} initialized with the given required {@link String name}
     * and {@link String domain extension}.
     * @throws IllegalArgumentException if the given {@link String name} or {@link String extension name}
     * is {@literal null} or {@literal empty}.
     */
    public static @NotNull Domain of(@NotNull String name, @NotNull String extensionName) {
      return new Domain(name, extensionName);
    }

    /**
     * Factory method used to construct a new {@link Domain} initialized by parsing a given {@link String}
     * representing a {@literal domain name}.
     *
     * @param domainName {@link String} containing the {@literal domain name} to parse;
     * must not be {@literal null} or {@literal empty}.
     * @return a new {@link Domain} parsed from the given, required {@link String}
     * representing a {@link String domain name}.
     * @throws IllegalArgumentException if the given {@link String domain name} is {@literal null} or {@literal empty},
     * or the {@link String domain name} format is not valid.
     */
    public static @NotNull Domain parse(@NotNull String domainName) {

      int indexOfDot = assertDomainName(domainName);

      String name = domainName.substring(0, indexOfDot);
      String extensionName = domainName.substring(indexOfDot + 1);

      return new Domain(name, extensionName);
    }

    private static int assertDomainName(String domainName) {

      Assert.hasText(domainName, "Domain Name [%s] to parse is required", domainName);

      int indexOfDot = domainName.lastIndexOf(DOMAIN_DOT_SEPARATOR);

      Assert.isTrue(indexOfDot > 0, "Domain Name [%s] format is not valid", domainName);

      return indexOfDot;
    }

    private static String requireExtensionName(String extensionName) {
      return StringUtils.requireText(extensionName, "Extension [%s] is required");
    }

    private static Extension resolveExtension(String extensionName) {

      String resolvedExtensionName = requireExtensionName(extensionName).toLowerCase();

      return Extensions.from(extensionName)
        .orElseGet(() -> Extension.named(resolvedExtensionName));
    }

    private final String name;

    private final Extension extension;

    /**
     * Constructs a new {@link Domain} initialized with the given {@link String name} and {@link Extension}.
     *
     * @param name {@link String name} of this {@link Domain}; must not be {@literal null} or {@literal empty}.
     * @param extension {@link Extension} of this {@link Domain}.
     * @throws IllegalArgumentException if the given {@link String name} is {@literal null} or {@literal empty},
     * or {@link Extension} is {@literal null}.
     * @see org.cp.domain.contact.email.model.EmailAddress.Domain.Extension
     */
    public Domain(@NotNull String name, @NotNull Extension extension) {

      this.name = StringUtils.requireText(name, "Name [%s] is required");
      this.extension = ObjectUtils.requireObject(extension, "Domain Extension is required");
    }

    /**
     * Constructs a new {@link Domain} initialized with the given, required {@link String name}
     * and {@link String domain extension}.
     *
     * @param name {@link String name} of this {@link Domain}; must not be {@literal null} or {@literal empty}.
     * @param extensionName {@link String extension name} of this {@link Domain};
     * must not be {@literal null} or {@literal empty}.
     * @throws IllegalArgumentException if the given {@link String name} or {@link String extension name}
     * is {@literal null} or {@literal empty}.
     */
    public Domain(@NotNull String name, @NotNull String extensionName) {
      this(name, resolveExtension(extensionName));
    }

    /**
     * Gets the {@link String name} of this {@link Domain}.
     *
     * @return the {@link String name} of this {@link Domain}.
     */
    @Override
    public @NotNull String getName() {
      return this.name;
    }

    /**
     * Returns an {@link Optional} {@link Extensions} modeling this {@link Domain Domain's}
     * {@link #getExtensionName() extension}.
     *
     * @return an {@link Optional} {@link Extensions} modeling this {@link Domain Domain's}
     * {@link #getExtensionName() extension}.
     * @see Extensions
     * @see #getExtensionName()
     * @see Optional
     */
    @SuppressWarnings("all")
    public Extension getExtension() {
      return this.extension;
    }

    /**
     * Gets the {@link String extension name} of this {@link Domain}.
     *
     * @return the {@link String extension name} of this {@link Domain}.
     */
    public @NotNull String getExtensionName() {
      return getExtension().getName();
    }

    @Override
    @SuppressWarnings("all")
    protected @NotNull Object clone() throws CloneNotSupportedException {
      return from(this);
    }

    @Override
    public int compareTo(@NotNull Domain that) {

      return ComparatorResultBuilder.<String>create()
        .doCompare(this.getExtensionName(), that.getExtensionName())
        .doCompare(this.getName(), that.getName())
        .build();
    }

    @Override
    public boolean equals(@Nullable Object obj) {

      if (this == obj) {
        return true;
      }

      if (!(obj instanceof Domain that)) {
        return false;
      }

      return ObjectUtils.equals(this.getName(), that.getName())
        && StringUtils.equalsIgnoreCase(this.getExtensionName(), that.getExtensionName());
    }

    @Override
    public int hashCode() {
      return ObjectUtils.hashCodeOf(getName(), getExtensionName());
    }

    @Override
    public @NotNull String toString() {
      String domainName = getName();
      String extension = getExtensionName().toLowerCase();
      return DOMAIN_TO_STRING.formatted(domainName, extension);
    }

    /**
     * Abstract Data Type (ADT) modeling a domain extension, such as {@literal .com}.
     *
     * @see java.lang.FunctionalInterface
     * @see java.io.Serializable
     * @see org.cp.elements.lang.Nameable
     * @see Extensions
     */
    @FunctionalInterface
    public interface Extension extends Nameable<String>, Serializable {

      /**
       * Factory method used to construct an {@link Extension} with the given {@link String name}.
       *
       * @param name {@link String name} of this {@link Extension}; required.
       * @return a new {@link Extension} with the given {@link String name}.
       * @throws IllegalArgumentException if {@link String name} is {@literal null} or {@literal empty}.
       */
      @JsonCreator
      static Extension named(@NotNull String name) {
        Assert.hasText(name, "Name [%s] is required", name);
        return () -> name;
      }

      /**
       * Gets an {@link Optional} {@link Extensions common extenstion} if known.
       *
       * @return an {@link Optional} {@link Extensions common extension} if known.
       * @see org.cp.domain.contact.email.model.EmailAddress.Domain.Extensions
       * @see java.util.Optional
       */
      default Optional<Extension> getExt() {
        return this instanceof Extensions extension ? Optional.of(extension)
          : Extensions.from(getName());
      }
    }

    /**
     * {@link Enum Enumeration} of common {@link Domain} {@link Extension Extensions}.
     *
     * @see java.lang.Enum
     * @see Extension
     */
    public enum Extensions implements Extension {

      BIZ,
      CO,
      COM,
      DE,
      EDU,
      GOV,
      INFO,
      IO,
      ME,
      NET,
      ORG,
      SITE,
      UK,
      US,
      XYZ;

      public static Optional<Extension> from(@Nullable String domainName) {

        return Optional.ofNullable(domainName)
          .filter(StringUtils::hasText)
          .map(Extensions::trimmedLowerCase)
          .map(Extensions::stripName)
          .flatMap(it -> Arrays.stream(Extensions.values())
            .filter(ext -> it.endsWith(ext.getName()))
            .findFirst());
      }

      private static String stripName(String domainName) {
        int beginIndex = domainName.lastIndexOf(StringUtils.DOT_SEPARATOR) + 1;
        return domainName.substring(beginIndex);
      }

      private static String trimmedLowerCase(String value) {
        return StringUtils.toLowerCase(StringUtils.trim(value));
      }

      @Override
      public String getName() {
        return name().toLowerCase();
      }
    }
  }
}
