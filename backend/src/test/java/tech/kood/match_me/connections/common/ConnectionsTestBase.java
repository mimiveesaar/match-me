package tech.kood.match_me.connections.common;

import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.TestBase;

@Transactional(transactionManager = "connectionsTransactionManager")
public abstract class ConnectionsTestBase extends TestBase {
}