package tech.kood.match_me.user_management.common;

import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.TestBase;

@Transactional(transactionManager = "userManagementTransactionManager")
public abstract class UserManagementTestBase extends TestBase {
}
