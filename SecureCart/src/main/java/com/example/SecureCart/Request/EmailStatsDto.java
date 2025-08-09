package com.example.SecureCart.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class EmailStatsDto {
	private final long pendingEmails;
    private final long failedEmails;
    private final long sentEmails;
    private final boolean healthy;
    
    
}
