package edu.ntnu.idi.stud.team10.sparesti.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ntnu.idi.stud.team10.sparesti.dto.BudgetDto;
import edu.ntnu.idi.stud.team10.sparesti.dto.BudgetRowDto;
import edu.ntnu.idi.stud.team10.sparesti.dto.UserDto;
import edu.ntnu.idi.stud.team10.sparesti.model.Budget;
import edu.ntnu.idi.stud.team10.sparesti.model.BudgetRow;
import edu.ntnu.idi.stud.team10.sparesti.model.User;
import edu.ntnu.idi.stud.team10.sparesti.repository.BudgetRepository;
import edu.ntnu.idi.stud.team10.sparesti.repository.BudgetRowRepository;
import edu.ntnu.idi.stud.team10.sparesti.repository.UserRepository;
import edu.ntnu.idi.stud.team10.sparesti.util.NotFoundException;

/** Service for Budget entities that are connected to a user. */
@Service
public class UserBudgetService {
  private final UserRepository userRepository;

  private final BudgetRepository budgetRepository;

  private final BudgetRowRepository budgetRowRepository;

  @Autowired
  public UserBudgetService(
      UserRepository userRepository,
      BudgetRepository budgetRepository,
      BudgetRowRepository budgetRowRepository) {
    this.userRepository = userRepository;
    this.budgetRepository = budgetRepository;
    this.budgetRowRepository = budgetRowRepository;
  }

  /**
   * Adds a budget to a user.
   *
   * @param userId the user id to add the budget for.
   * @param budgetDto the budget to add.
   * @return the updated user.
   */
  public UserDto addBudgetToUser(Long userId, BudgetDto budgetDto) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new NotFoundException("User with ID " + userId + " not found"));

    Budget budget = budgetDto.toEntity();
    budget.setUser(user);
    budgetRepository.save(budget);
    return new UserDto(user);
  }

  /**
   * Gets all budgets for a user.
   *
   * @param userId the user id to get budgets for.
   * @return a list of budget DTOs.
   */
  public List<BudgetDto> getAllBudgetsForUser(Long userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new NotFoundException("User with ID " + userId + " not found"));

    return budgetRepository.findByUser(user).stream()
        .map(BudgetDto::new)
        .collect(Collectors.toList());
  }

  /**
   * Deletes a budget from a user.
   *
   * @param userId the user id to delete the budget from.
   * @param budgetId the budget id to delete.
   */
  public void deleteBudgetFromUser(Long userId, Long budgetId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new NotFoundException("User with ID " + userId + " not found"));

    Budget budget =
        budgetRepository
            .findById(budgetId)
            .orElseThrow(() -> new NotFoundException("Budget with ID " + budgetId + " not found"));

    budgetRepository.delete(budget);
  }

  /**
   * Adds a budget row to a user's budget.
   *
   * @param userId the user id to add the budget row for.
   * @param budgetId the budget id to add the budget row for.
   * @param budgetRowDto the budget row to add.
   * @return the updated budget.
   */
  public BudgetDto addBudgetRowToUserBudget(Long userId, Long budgetId, BudgetRowDto budgetRowDto) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new NotFoundException("User with ID " + userId + " not found"));

    Budget budget =
        budgetRepository
            .findById(budgetId)
            .orElseThrow(() -> new NotFoundException("Budget with ID " + budgetId + " not found"));

    if (!budget.getUser().equals(user)) {
      throw new IllegalArgumentException("The budget does not belong to the user");
    }

    BudgetRow budgetRow = budgetRowDto.toEntity();
    budgetRow.setBudget(budget);
    budget.getRow().add(budgetRow);
    budgetRepository.save(budget);

    return new BudgetDto(budget);
  }

  /**
   * Deletes a budget row from a user's budget.
   *
   * @param userId the user id to delete the budget row for.
   * @param budgetId the budget id to delete the budget row for.
   * @param budgetRowId the budget row id to delete.
   */
  public void deleteBudgetRowFromUserBudget(Long userId, Long budgetId, Long budgetRowId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new NotFoundException("User with ID " + userId + " not found"));

    Budget budget =
        budgetRepository
            .findById(budgetId)
            .orElseThrow(() -> new NotFoundException("Budget with ID " + budgetId + " not found"));

    if (!budget.getUser().equals(user)) {
      throw new IllegalArgumentException("The budget does not belong to the user");
    }

    BudgetRow budgetRow =
        budgetRowRepository
            .findById(budgetRowId)
            .orElseThrow(
                () -> new NotFoundException("BudgetRow with ID " + budgetRowId + " not found"));

    budget.getRow().remove(budgetRow);
    budgetRepository.save(budget);
  }

  /**
   * Edits a budget row in a user's budget.
   *
   * @param userId the user id to edit the budget row for.
   * @param budgetId the budget id to edit the budget row for.
   * @param budgetRowId the budget row id to edit.
   * @param budgetRowDto the new data for the budget row.
   * @return the updated budget row.
   */
  public BudgetRowDto editBudgetRowInUserBudget(
      Long userId, Long budgetId, Long budgetRowId, BudgetRowDto budgetRowDto) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new NotFoundException("User with ID " + userId + " not found"));

    Budget budget =
        budgetRepository
            .findById(budgetId)
            .orElseThrow(() -> new NotFoundException("Budget with ID " + budgetId + " not found"));

    if (!budget.getUser().equals(user)) {
      throw new IllegalArgumentException("The budget does not belong to the user");
    }

    BudgetRow budgetRow =
        budgetRowRepository
            .findById(budgetRowId)
            .orElseThrow(
                () -> new NotFoundException("BudgetRow with ID " + budgetRowId + " not found"));

    // Update the budget row with the new data
    budgetRow.updateFromDto(budgetRowDto);
    budgetRowRepository.save(budgetRow);

    return new BudgetRowDto(budgetRow);
  }
}