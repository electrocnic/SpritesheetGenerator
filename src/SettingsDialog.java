import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Andreas on 02.01.2016.
 */
public class SettingsDialog extends JDialog implements ActionListener {

    private JButton button_prev = null;
    private JButton button_next = null;
    private ExportPrompt exportPrompt = null;
    private SpritesheetGenerator controller = null;

    private JPanel panel_content = null;
    private JPanel panel_buttons = null;

    public SettingsDialog( SpritesheetGenerator controller ) {
        super( controller.getView(), "Settings for directories", true );
        this.controller = controller;
        panel_content = new JPanel( new BorderLayout(5,5));
        exportPrompt = new ExportPrompt( controller, controller.getView() );

        panel_content.add( exportPrompt, BorderLayout.NORTH );

        panel_buttons = new JPanel( new BorderLayout(5,5));
        button_prev = new JButton("Previous");
        button_prev.addActionListener( this );
        button_next = new JButton(controller.isLast()?"Generate":"Next");
        button_next.addActionListener( this );
        panel_buttons.add(button_prev, BorderLayout.WEST);
        panel_buttons.add( button_next, BorderLayout.EAST);

        panel_content.add( panel_buttons, BorderLayout.SOUTH );
        panel_content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        getContentPane().add(panel_content);
        //setLocationRelativeTo(controller.getView());
        Point location = new Point( controller.getView().getX(), controller.getView().getY() );
        setLocation( location );

        setPreferredSize(new Dimension(ExportPrompt.WIDTH, ExportPrompt.HEIGHT));
        setSize(ExportPrompt.WIDTH+20, ExportPrompt.HEIGHT+20);

        next();
        setVisible(true);
        repaint();
    }

    private void next() {
        if( controller.isLast() ) button_next.setText( "Generate" );
        else button_next.setText( "Next" );
        exportPrompt.next(controller.getCurrentNode(), controller.isLast());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == button_next ) {
            System.out.println("Next clicked");
            exportPrompt.updateNode();
            if( button_next.getText().equalsIgnoreCase("Generate")) {
                dispose();
                controller.finallyExport();
            }else {
                if(exportPrompt.nextPressed()) next();
            }
        }else if( e.getSource() == button_prev ) {
            exportPrompt.updateNode();
            if(controller.isFirst()) dispose();
            else {
                controller.previousDirectory();
                next();
            }
        }
    }
}
