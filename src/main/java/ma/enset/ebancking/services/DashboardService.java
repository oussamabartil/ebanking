package ma.enset.ebancking.services;

import ma.enset.ebancking.dtos.DashboardStatsDTO;

public interface DashboardService {
    DashboardStatsDTO getDashboardStatistics();
    DashboardStatsDTO getDashboardStatistics(String period); // daily, weekly, monthly, yearly
}
