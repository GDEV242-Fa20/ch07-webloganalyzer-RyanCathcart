/**
 * Read web server data and analyse hourly access patterns.
 * 
 * @author David J. Barnes and Michael Kölling.
 * @version    2016.02.29
 */
public class LogAnalyzer
{
    // Where to calculate the monthly access counts.
    private int[] monthCounts;
    // Where to calculate the daily access counts.
    private int[] dayCounts;
    // Where to calculate the hourly access counts.
    private int[] hourCounts;
    // Use a LogfileReader to access the data.
    private LogfileReader reader;

    /**
     * Create an object to analyze hourly web accesses.
     */
    public LogAnalyzer()
    {
        // Create the array object to hold the monthly
        // access counts.
        monthCounts = new int[12];
        // Create the array object to hold the daily
        // access counts.
        dayCounts = new int[28];
        // Create the array object to hold the hourly
        // access counts.
        hourCounts = new int[24];
        // Create the reader to obtain the data.
        reader = new LogfileReader("demo.log");
    }
    
    /**
     * Create an object to analyze hourly web accesses using a given
     * file name.
     * @param filename The file of log data.
     */
    public LogAnalyzer(String filename) {
        // Create the array object to hold the monthly
        // access counts.
        monthCounts = new int[12];
        // Create the array object to hold the daily
        // access counts.
        dayCounts = new int[28];
        // Create the array object to hold the hourly
        // access counts.
        hourCounts = new int[24];
        // Create the reader to obtain the data.
        reader = new LogfileReader(filename);
    }
    
    /**
     * Analyze the hourly, daily, and monthly
     * access data from the log file.
     */
    public void analyzeAllData()
    {
        analyzeMonthlyData();
        analyzeDailyData();
        analyzeHourlyData();
    }
    
    /**
     * Analyze the monthly access data from the log file.
     */
    public void analyzeMonthlyData()
    {
        reader.reset();
        while(reader.hasNext()) {
            LogEntry entry = reader.next();
            int month = entry.getMonth();
            monthCounts[month - 1]++;
        }
    }
    
    /**
     * Analyze the daily access data from the log file.
     */
    public void analyzeDailyData()
    {
        reader.reset();
        while(reader.hasNext()) {
            LogEntry entry = reader.next();
            int day = entry.getDay();
            dayCounts[day - 1]++;
        }
    }
    
    /**
     * Analyze the hourly access data from the log file.
     */
    public void analyzeHourlyData()
    {
        reader.reset();
        while(reader.hasNext()) {
            LogEntry entry = reader.next();
            int hour = entry.getHour();
            hourCounts[hour]++;
        }
    }
    
    /**
     * Print the monthly counts.
     * These should have been set with a prior
     * call to analyzeMonthlyData.
     */
    public void printMonthlyCounts()
    {
        System.out.println("Month: Count");
        for(int month = 0; month < monthCounts.length; month++) {
            System.out.println((month + 1) + ": " + monthCounts[month]);
        }
    }
    
    /**
     * Print the daily counts.
     * These should have been set with a prior
     * call to analyzeDailyData.
     */
    public void printDailyCounts()
    {
        System.out.println("Day: Count");
        for(int day = 0; day < dayCounts.length; day++) {
            System.out.println((day + 1) + ": " + dayCounts[day]);
        }
    }

    /**
     * Print the hourly counts.
     * These should have been set with a prior
     * call to analyzeHourlyData.
     */
    public void printHourlyCounts()
    {
        System.out.println("Hr: Count");
        for(int hour = 0; hour < hourCounts.length; hour++) {
            System.out.println(hour + ": " + hourCounts[hour]);
        }
    }
    
    /**
     * Print the lines of data read by the LogfileReader
     */
    public void printData()
    {
        reader.printData();
    }
    
    /**
     * Return the number of accesses recorded in the log file.
     * @return the total number of accesses.
     */
    public int numberOfAccesses() {
        int total = 0;
        for (int i=0;i<hourCounts.length;i++) {
            total += hourCounts[i];
        }
        return total;
    }
    
    /**
     * Return the busiest hour recorded in the log file.
     * @return The hour with the most accesses.
     */
    public int busiestHour() {
        int busiestHour = 0;
        for (int i=1;i<hourCounts.length;i++) {
            if (hourCounts[i] > hourCounts[busiestHour])
                busiestHour = i;
        }
        return busiestHour;
    }
    
    /**
     * Return the quietest hour recorded in the log file.
     * @return The hour with the least accesses.
     */
    public int quietestHour() {
        int quietestHour = 0;
        for (int i=1;i<hourCounts.length;i++) {
            if (hourCounts[i] < hourCounts[quietestHour]) 
                quietestHour = i;
        }
        return quietestHour;
    }
    
    /**
     * Return the buiest two-hour period recorded in the log file.
     * @return The starting hour of the two-hour period with the most accesses.
     */
    public int busiestTwoHour() {
        int busiestTwoHour = 0;
        int busiestTwoHourAmt = hourCounts[0] + hourCounts[1] + hourCounts[2];
        int arrLen = hourCounts.length;
        for (int i=1;i<arrLen;i++) {
            int currentTwoHours = hourCounts[i % arrLen] + 
                                  hourCounts[(i+1) % arrLen] + 
                                  hourCounts[(i+2) % arrLen];
            if (currentTwoHours > busiestTwoHourAmt) {
                busiestTwoHour = i;
                busiestTwoHourAmt = currentTwoHours;
            }
        }
        return busiestTwoHour;
    }
    
    /**
     * Return the quietest day recorded in the log file.
     * @return The day with the least accesses.
     */
    public int quietestDay() {
        int quietestDay = 0;
        for (int i=1;i<dayCounts.length;i++) {
            if (dayCounts[i] < dayCounts[quietestDay]) 
                quietestDay = i;
        }
        return quietestDay + 1; // Must compensate for arrays starting at 0 and days starting at 1.
    }
    
    /**
     * Return the busiest day recorded in the log file.
     * @return The day with the most accesses.
     */
    public int busiestDay() {
        int busiestDay = 0;
        for (int i=1;i<dayCounts.length;i++) 
            if (dayCounts[i] > dayCounts[busiestDay]) {
                busiestDay = i;
        }
        return busiestDay + 1; // Must compensate for arrays starting at 0 and days starting at 1.
    }
    
    /**
     * Return the quietest month recorded in the log file.
     * @return The month with the least accesses.
     */
    public int quietestMonth() {
        int quietestMonth = 0;
        for (int i=1;i<monthCounts.length;i++) {
            if (monthCounts[i] < monthCounts[quietestMonth]) 
                quietestMonth = i;
        }
        return quietestMonth + 1; // Must compensate for arrays starting at 0 and months starting at 1.
    }
    
    /**
     * Return the busiest month recorded in the log file.
     * @return The month with the most accesses.
     */
    public int busiestMonth() {
        int busiestMonth = 0;
        for (int i=1;i<monthCounts.length;i++) {
            if (monthCounts[i] > monthCounts[busiestMonth]) 
                busiestMonth = i;
        }
        return busiestMonth + 1; // Must compensate for arrays starting at 0 and months starting at 1.
    }
    
    /**
     * Return the total number of accesses per month recorded in the log file.
     * @return The number of accesses for each month.
     */
    public int[] totalAccessesPerMonth() {
        return monthCounts;
    }
    
    /**
     * Return the average number of accesses per month recorded in the log file. 
     * Averages are based on accesses per month each year.
     * @return The averages for each month over the span of the 5 years.
     */
    public int[] averageAccessesPerMonth() {
        int [] avgs = new int[12];
        for (int i=0;i<avgs.length;i++) {
            avgs[i] = monthCounts[i] / 5;
        }
        return avgs;
    }
}
