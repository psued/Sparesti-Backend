package edu.ntnu.idi.stud.team10.sparesti.model;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.ntnu.idi.stud.team10.sparesti.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;

/** Entity representing a user in the database. */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String displayName;

  @Column(nullable = false)
  private String password;

  @Column(unique = true)
  private String email;

  @Column private double totalSavings;

  // TODO: Add "Total amount saved" to the user?

  @Column() private String profilePictureUrl;

  @Column() private Integer checkingAccountNr;

  @Column() private Integer savingsAccountNr;

  @JsonIgnore
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<SavingsGoal> savingsGoals;

  @ManyToMany
  @JoinTable(
      name = "user_challenge",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "challenge_id"))
  private List<Challenge> challenges;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_badges",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "badge_id"))
  private Set<Badge> earnedBadges;

  /**
   * Constructor for converting UserDto to User.
   *
   * @param dto (UserDto) A Dto representing the user.
   */
  public User(UserDto dto) {
    this.id = dto.getId();
    this.displayName = dto.getDisplayName();
    this.password = dto.getPassword();
    this.email = dto.getEmail();
    this.profilePictureUrl = dto.getProfilePictureUrl();
    this.totalSavings = dto.getTotalSavings();
  }

  /**
   * Add a savings goal to the user.
   *
   * @param challenge (SavingsGoal) The savings goal to add.
   */
  public void addChallenge(Challenge challenge) {
    this.challenges.add(challenge);
  }

  /**
   * Remove a savings goal from the user.
   *
   * @param challenge (SavingsGoal) The savings goal to remove.
   */
  public void removeChallenge(Challenge challenge) {
    this.challenges.remove(challenge);
  }

  public void addBadge(Badge badge) {
    this.earnedBadges.add(badge);
  }

  public List<Badge> getEarnedBadges() {
    if (earnedBadges == null) {
      return List.of();
    } else {
      return List.copyOf(earnedBadges);
    }
  }

  public void removeBadge(Badge badge) {
    this.earnedBadges.remove(badge);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(id, user.id)
        && Objects.equals(displayName, user.displayName)
        && Objects.equals(password, user.password)
        && Objects.equals(email, user.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, displayName, password, email);
  }

  @Override
  public String toString() {
    return "User{"
        + "id="
        + id
        + ", displayName='"
        + displayName
        + '\''
        + ", email='"
        + email
        + '\''
        + '}';
  }
}
