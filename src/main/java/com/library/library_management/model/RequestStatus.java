package com.library.library_management.model;

public enum RequestStatus {
    PENDING,        // Waiting for Admin approval
    APPROVED,       // Approved by Admin, ready for issuance
    REJECTED,       // Rejected by Admin
    ISSUED          // Converted to an IssuedBook (loan)
}