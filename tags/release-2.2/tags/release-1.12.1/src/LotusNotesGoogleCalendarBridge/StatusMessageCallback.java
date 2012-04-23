package LotusNotesGoogleCalendarBridge;

/**
 * Defines the interface for displaying or logging status messages.
 */
public interface StatusMessageCallback {
    public void statusAppendLine(String text);
    public void statusAppend(String text);
    public void statusAppendLineDiag(String text);

    public void statusAppendStart(String text);
    public void statusAppendFinished();
    
    public void statusAppendException(String text, Exception ex);
}

