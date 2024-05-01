package edu.ntnu.idi.stud.team10.sparesti.dto;

import java.util.List;

import lombok.*;

/** Data transfer object for User entities. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
  private Long id;
  private String password;
  private String email;
  private String profilePictureUrl;
  private Long checkingAccountNr;
  private Long savingsAccountNr;
  private Double totalSavings;
  private List<ChallengeDto> challenges;
}
