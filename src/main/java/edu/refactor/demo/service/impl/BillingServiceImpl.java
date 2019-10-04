package edu.refactor.demo.service.impl;

import edu.refactor.demo.dao.BillingAccountDAO;
import edu.refactor.demo.entity.BillingAccount;
import edu.refactor.demo.exception.BillingAccountNotFoundException;
import edu.refactor.demo.service.BillingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class BillingServiceImpl implements BillingService {
    private static final Logger logger = LoggerFactory.getLogger(BillingServiceImpl.class);

    private BillingAccountDAO billingAccountDAO;

    @Autowired
    public BillingServiceImpl(BillingAccountDAO billingAccountDAO) {
        this.billingAccountDAO = billingAccountDAO;
    }

    @Override
    public boolean cashWithdrawal(BigDecimal money, Long accountId) {
        Optional<BillingAccount> accountOpt = billingAccountDAO.findById(accountId);

        if (!accountOpt.isPresent()) {
            throw new BillingAccountNotFoundException(
                    String.format("Billing account [%d] not found", accountId));
        }

        BillingAccount account = accountOpt.get();
        BigDecimal balance = account.getBalance();

        if(isAccountPayable(balance, money)) {
            BigDecimal availableBalance = balance.subtract(money);

            account.setBalance(availableBalance);

            billingAccountDAO.save(account);

            logger.info("Account[{}] balance is changed success", accountId);

            return true;
        }

        logger.info("Operation withdrawal is not success for account[{}]", accountId);

        return false;
    }

    private boolean isAccountPayable(BigDecimal balance, BigDecimal money) {
        logger.info("Check in payable account");

        return balance.compareTo(money) >= 0;
    }
}
