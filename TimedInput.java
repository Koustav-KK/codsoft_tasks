import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TimedInput
{
    private final BufferedReader reader =
            new BufferedReader(new InputStreamReader(System.in));


    public String readLine(long timeoutMillis)
    {
        long deadline = System.currentTimeMillis() + timeoutMillis;

        try {
            while (System.currentTimeMillis() < deadline)
            {
                if (reader.ready())
                {
                    return reader.readLine();
                }
                Thread.sleep(100);
            }
        } catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }catch (IOException e)
        {

        }
        return null;
    }

}
