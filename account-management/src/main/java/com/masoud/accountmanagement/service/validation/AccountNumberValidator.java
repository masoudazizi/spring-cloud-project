package com.masoud.accountmanagement.service.validation;

import com.masoud.accountmanagement.service.dto.AccountInfoDTO;
import com.masoud.accountmanagement.domain.AccountInfo;
import com.masoud.accountmanagement.repository.AccountInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class AccountNumberValidator implements ConstraintValidator<AccountNumberUniqueness, AccountInfoDTO> {

    @Autowired
    private AccountInfoRepository accountInfoRepository;

    @Override
    public boolean isValid(AccountInfoDTO accountInfoDTO, ConstraintValidatorContext constraintValidatorContext) {
        Optional<AccountInfo> accountInfoOptional = accountInfoRepository.findByAccountNumber(accountInfoDTO.getAccountNumber());
        if (accountInfoOptional.isPresent() && accountInfoDTO.getId() == null)
            return false;
        else if (accountInfoOptional.isPresent() && accountInfoDTO.getId() != null
                && !accountInfoOptional.get().getId().equals(accountInfoDTO.getId()))
            return false;
        else
            return true;
    }
}