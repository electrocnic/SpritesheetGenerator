import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A JTextField that accepts only integers.
 *
 * @author David Buzatto
 */
public class JIntegerField extends JTextField {

    static ActionListener listener = null;
    boolean virtualInput = false;

    public JIntegerField() {
        super();
    }

    public JIntegerField( String text ) {
        super( text );
    }

    public JIntegerField( int cols ) {
        super( cols );
    }

    public void setActionListener( ActionListener listener ) {
        this.listener = listener;
    }

    @Override
    protected Document createDefaultModel() {
        return new UpperCaseDocument( this );
    }



    static class UpperCaseDocument extends PlainDocument {

        JIntegerField master = null;

        public UpperCaseDocument( JIntegerField master ) {
            this.master = master;
        }

        @Override
        public void insertString( int offs, String str, AttributeSet a )
                throws BadLocationException {

            if ( str == null ) {
                return;
            }

            char[] chars = str.toCharArray();
            boolean ok = true;

            for ( int i = 0; i < chars.length; i++ ) {

                try {
                    Integer.parseUnsignedInt(String.valueOf(chars[i]));
                } catch ( NumberFormatException exc ) {
                    ok = false;
                    break;
                }
            }

            if ( ok ) {
                super.insertString(offs, new String(chars), a);

                if (listener != null && !master.virtualInput)
                    listener.actionPerformed(new ActionEvent(master, 1, "changed"));
            }
        }


    }

    /**
     *
     * @param string
     */
    @Override
    public void setText( String string ) {
        virtualInput = true;
        super.setText( string );
        virtualInput = false;
    }
}