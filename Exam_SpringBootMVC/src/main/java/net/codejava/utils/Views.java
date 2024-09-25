package net.codejava.utils;

public class Views {

    // Customers table
    public static final String TBL_CUSTOMERS = "Customers";
    public static final String COL_CUSTOMERS_ID = "customer_id";
    public static final String COL_CUSTOMERS_FULLNAME = "fullname";
    public static final String COL_CUSTOMERS_PASSWORD = "password";
    public static final String COL_CUSTOMERS_EMAIL = "email";
    public static final String COL_CUSTOMERS_PHONE = "phone";
    public static final String COL_CUSTOMERS_ADDRESS = "address";
    public static final String COL_CUSTOMERS_PROFILE_IMAGE = "profile_image";
    public static final String COL_CUSTOMERS_STATUS = "status";
    public static final String COL_CUSTOMERS_CREATED_AT = "created_at";
    public static final String COL_CUSTOMERS_VERIFY_CODE = "verify_code";
    public static final String COL_CUSTOMERS_TOKEN = "token";

    // Employees table
    public static final String TBL_EMPLOYEES = "Employees";
    public static final String COL_EMPLOYEES_ID = "employee_id";
    public static final String COL_EMPLOYEES_FULLNAME = "fullname";
    public static final String COL_EMPLOYEES_PASSWORD = "password";
    public static final String COL_EMPLOYEES_USER_TYPE = "user_type";
    public static final String COL_EMPLOYEES_EMAIL = "email";
    public static final String COL_EMPLOYEES_PHONE = "phone";
    public static final String COL_EMPLOYEES_ADDRESS = "address";
    public static final String COL_EMPLOYEES_PROFILE_IMAGE = "profile_image";
    public static final String COL_EMPLOYEES_EXPERIENCE_YEARS = "experience_years";
    public static final String COL_EMPLOYEES_SALARY = "salary";
    public static final String COL_EMPLOYEES_STATUS = "status";
    public static final String COL_EMPLOYEES_VERIFY_CODE = "verify_code";
    public static final String COL_EMPLOYEES_TOKEN = "token";
    public static final String COL_EMPLOYEES_CREATED_AT = "created_at";

    // Services table
    public static final String TBL_SERVICES = "Services";
    public static final String COL_SERVICES_ID = "service_id";
    public static final String COL_SERVICES_NAME = "service_name";
    public static final String COL_SERVICES_DESCRIPTION = "service_description";
    public static final String COL_SERVICES_PRICE = "service_price";
    public static final String COL_SERVICES_STATUS = "service_status";
    public static final String COL_SERVICES_IMAGE = "service_image";

    // Employee_Services table
    public static final String TBL_EMPLOYEE_SERVICES = "Employee_Services";
    public static final String COL_EMPLOYEE_SERVICES_ID = "emp_service_id";
    public static final String COL_EMPLOYEE_SERVICES_EMPLOYEE_ID = "employee_id";
    public static final String COL_EMPLOYEE_SERVICES_SERVICE_ID = "service_id";
    public static final String COL_EMPLOYEE_SERVICES_DETAILS = "details";

    // Contracts table
    public static final String TBL_CONTRACTS = "Contracts";
    public static final String COL_CONTRACTS_ID = "contract_id";
    public static final String COL_CONTRACTS_CUSTOMER_ID = "customer_id";
    public static final String COL_CONTRACTS_STATUS = "contract_status";
    public static final String COL_CONTRACTS_TOTAL_PRICE = "total_price";
    public static final String COL_CONTRACTS_PAYMENT_STATUS = "payment_status";
    public static final String COL_CONTRACTS_CREATED_AT = "created_at";
    public static final String COL_CONTRACTS_FILE = "contract_file";

    // Contract_Details table
    public static final String TBL_CONTRACT_DETAILS = "Contract_Details";
    public static final String COL_CONTRACT_DETAILS_ID = "contract_detail_id";
    public static final String COL_CONTRACT_DETAILS_CONTRACT_ID = "contract_id";
    public static final String COL_CONTRACT_DETAILS_EMP_SERVICE_ID = "emp_service_id";
    public static final String COL_CONTRACT_DETAILS_SERVICE_ADDRESS = "service_address";
    public static final String COL_CONTRACT_DETAILS_SERVICE_PHONE = "service_phone";
    public static final String COL_CONTRACT_DETAILS_START_DATE = "start_date";
    public static final String COL_CONTRACT_DETAILS_END_DATE = "end_date";
    public static final String COL_CONTRACT_DETAILS_STATUS = "status";
    public static final String COL_CONTRACT_DETAILS_HOURS_WORKED = "hours_worked";
    public static final String COL_CONTRACT_DETAILS_TOTAL_PRICE = "total_price";

    // Contract_Refusals table
    public static final String TBL_CONTRACT_REFUSALS = "Contract_Refusals";
    public static final String COL_CONTRACT_REFUSALS_ID = "refusal_id";
    public static final String COL_CONTRACT_REFUSALS_CONTRACT_DETAIL_ID = "contract_detail_id";
    public static final String COL_CONTRACT_REFUSALS_REASON = "refusal_reason";
    public static final String COL_CONTRACT_REFUSALS_STATUS = "status";
    public static final String COL_CONTRACT_REFUSALS_DATE = "refusal_date";
    public static final String COL_CONTRACT_REFUSALS_CREATED_AT = "created_at";

    // Employee_Reviews table
    public static final String TBL_EMPLOYEE_REVIEWS = "Employee_Reviews";
    public static final String COL_EMPLOYEE_REVIEWS_ID = "review_id";
    public static final String COL_EMPLOYEE_REVIEWS_CONTRACT_DETAIL_ID = "contract_detail_id";
    public static final String COL_EMPLOYEE_REVIEWS_RATING = "rating";
    public static final String COL_EMPLOYEE_REVIEWS_COMMENT = "comment";
    public static final String COL_EMPLOYEE_REVIEWS_CREATED_AT = "created_at";

    // Salary_Proposals table
    public static final String TBL_SALARY_PROPOSALS = "Salary_Proposals";
    public static final String COL_SALARY_PROPOSALS_ID = "proposal_id";
    public static final String COL_SALARY_PROPOSALS_EMPLOYEE_ID = "employee_id";
    public static final String COL_SALARY_PROPOSALS_PROPOSED_SALARY = "proposed_salary";
    public static final String COL_SALARY_PROPOSALS_FROM_DATE = "from_date";
    public static final String COL_SALARY_PROPOSALS_TO_DATE = "to_date";
    public static final String COL_SALARY_PROPOSALS_APPROVAL_STATUS = "approval_status";
    public static final String COL_SALARY_PROPOSALS_CREATED_AT = "created_at";

    // Employee_Contract_Schedule table
    public static final String TBL_EMPLOYEE_CONTRACT_SCHEDULE = "Employee_Contract_Schedule";
    public static final String COL_EMPLOYEE_CONTRACT_SCHEDULE_ID = "schedule_id";
    public static final String COL_EMPLOYEE_CONTRACT_SCHEDULE_CONTRACT_DETAIL_ID = "contract_detail_id";
    public static final String COL_EMPLOYEE_CONTRACT_SCHEDULE_WORK_DATE = "work_date";
    public static final String COL_EMPLOYEE_CONTRACT_SCHEDULE_START_TIME = "start_time";
    public static final String COL_EMPLOYEE_CONTRACT_SCHEDULE_END_TIME = "end_time";
    public static final String COL_EMPLOYEE_CONTRACT_SCHEDULE_HOURS_WORKED = "hours_worked";
    public static final String COL_EMPLOYEE_CONTRACT_SCHEDULE_STATUS = "status";
}


