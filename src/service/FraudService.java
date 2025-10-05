package service;

import DAO.CardDAO;
import DAO.FraudAlertDAO;
import DAO.OperationDAO;

import java.util.List;

public class FraudService {
    private final FraudAlertDAO fraudAlertDAO;

    public FraudService(FraudAlertDAO fraudAlertDAO) {
        this.fraudAlertDAO = fraudAlertDAO;
    }

    public List<Object[]> getAllFraudHistory() {
        return fraudAlertDAO.findAll();
    }

}
