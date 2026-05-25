package es.codeurjc.service.loan;

import es.codeurjc.model.LoanEvaluationResult;

public class LoanApprovalAlgorithm {

    private static final double MINIMUM_AMOUNT = 500.0;
    private static final double MAXIMUM_AMOUNT = 50000.0;
    private static final int MINIMUM_TERM_MONTHS = 6;
    private static final int MAXIMUM_TERM_MONTHS = 120;
    private static final double MAXIMUM_PAYMENT_RATIO = 0.4;
    private static final double BASE_INTEREST_RATE = 4.5;
    private static final double LOYAL_CUSTOMER_DISCOUNT = 0.5;
    private static final double MULTI_PRODUCT_DISCOUNT = 0.25;
    private static final double LOW_BALANCE_RISK_SURCHARGE = 1.0;

    public LoanEvaluationResult evaluate(LoanRequest request) {
        if (request.getAmount() < MINIMUM_AMOUNT) {
            return rejected("Requested amount is below the minimum allowed");
        }

        if (request.getAmount() > MAXIMUM_AMOUNT) {
            return rejected("Requested amount exceeds the maximum allowed");
        }

        if (request.getTermMonths() < MINIMUM_TERM_MONTHS || request.getTermMonths() > MAXIMUM_TERM_MONTHS) {
            return rejected("Loan term is outside the allowed range");
        }

        double interestRate = calculateInterestRate(request);
        double monthlyPayment = calculateMonthlyPayment(request.getAmount(), request.getTermMonths(), interestRate);

        if (request.getMonthlyIncome() <= 0 || monthlyPayment > request.getMonthlyIncome() * MAXIMUM_PAYMENT_RATIO) {
            return rejected("Monthly payment exceeds customer repayment capacity");
        }

        return new LoanEvaluationResult(
                true,
                "Loan approved",
                request.getAmount(),
                interestRate,
                monthlyPayment);
    }

    private LoanEvaluationResult rejected(String reason) {
        return new LoanEvaluationResult(false, reason);
    }

    private double calculateInterestRate(LoanRequest request) {
        double interestRate = BASE_INTEREST_RATE;

        if (request.getYearsWithBank() >= 5) {
            interestRate -= LOYAL_CUSTOMER_DISCOUNT;
        }

        if (request.isHasMultipleProducts()) {
            interestRate -= MULTI_PRODUCT_DISCOUNT;
        }

        if (request.getCustomerBalance() < request.getAmount() * 0.1) {
            interestRate += LOW_BALANCE_RISK_SURCHARGE;
        }

        return interestRate;
    }

    private double calculateMonthlyPayment(double amount, int termMonths, double interestRate) {
        double totalAmount = amount * (1 + interestRate / 100);
        return totalAmount / termMonths;
    }
}
