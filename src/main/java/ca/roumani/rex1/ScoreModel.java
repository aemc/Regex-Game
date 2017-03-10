package ca.roumani.rex1;

public class ScoreModel
{
    private int attempts;
    private int successes;
    private long startTime;
    private long elapsedTime;

    public ScoreModel()
    {
        attempts = 0;
        successes = 0;
        startTime = System.currentTimeMillis();
    }

    public int getAttempts()
    {
        return attempts;
    }

    public int getSuccess()
    {
        return successes;
    }

    public long getStart()
    {
        return startTime;
    }

    public long getElapsedTime()
    {
        elapsedTime = System.currentTimeMillis();
        long difference = (elapsedTime - startTime) / 1000;
        return difference;
    }

    public void record(boolean success)
    {
        if (success) successes++;
        attempts++;
    }

    public double getAverageScore()
    {
        return ((double) successes / attempts) * 100;
    }

    public void resetTimer()
    {
        startTime = System.currentTimeMillis();
    }

    public static void main(String[] args)
    {
        ScoreModel sm = new ScoreModel();
        System.out.println("Start TIme: " + sm.getStart());
        sm.record(false);
        sm.record(false);
        sm.record(true);
        sm.record(false);
        System.out.println("Elapsed Time: " + sm.getElapsedTime() + "ms");
        System.out.println("Attempts: " + sm.getAttempts());
        System.out.println("Success: " + sm.getSuccess());
        System.out.println("Average Score: " + sm.getAverageScore());
        sm.resetTimer();
    }
}