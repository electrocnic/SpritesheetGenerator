import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by Andreas on 01.01.2016.
 */
public class ExportPrompt extends JFrame implements ActionListener{

    public static final int WIDTH = 600;
    public static final int HEIGHT = 400;

    private SpritesheetGenerator controller = null;

    private JFileChooser fileChooser = null;
    private JLabel label_directory_title = null;
    private JLabel label_directory = null;
    private JLabel label_information_sprites_title = null;
    private JLabel label_information_sprites = null;
    private JLabel label_customFilter = null;
    private JIntegerField textField_customFilter = null;
    private JButton button_browse = null;
    private JLabel label_destinationDirectory = null;
    private JTextField textField_destinationDirectory = null;
    private JCheckBox checkBox_isActive = null;
    private JLabel label_isActive = null;
    private JButton button_next = null;
    private JButton button_previous = null;

    private JPanel panel_00_directory = null;
    private JPanel panel_02_destinationDirectory = null;
    private JPanel panel_03_information = null;
    private JPanel panel_04_customFilter = null;
    private JPanel panel_05_isActive = null;
    private JPanel panel_06_buttons = null;


    public ExportPrompt( SpritesheetGenerator controller ) {
        this.controller = controller;
        setResizable(false);
        setSize(WIDTH, HEIGHT);

        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("E:\\Projects\\sketches\\flea_new\\png\\final"));


        label_directory_title = new JLabel("Import Directory: ");
        label_directory = new JLabel( "" );

        panel_00_directory = new JPanel(new BorderLayout(5,5));
        panel_00_directory.add( label_directory_title, BorderLayout.WEST );
        panel_00_directory.add( label_directory, BorderLayout.EAST );


        label_information_sprites_title = new JLabel("Number of Sprites in this directory:");
        label_information_sprites = new JLabel("");

        panel_03_information = new JPanel( new BorderLayout(5,5));
        panel_03_information.add( label_information_sprites_title, BorderLayout.WEST );
        panel_03_information.add( label_information_sprites, BorderLayout.EAST );


        label_customFilter = new JLabel("Custom Filter for this Directory:");
        textField_customFilter = new JIntegerField("");
        textField_customFilter.setPreferredSize( new Dimension(35,25));

        panel_04_customFilter = new JPanel( new BorderLayout(5,5));
        panel_04_customFilter.add( label_customFilter, BorderLayout.WEST );
        panel_04_customFilter.add( textField_customFilter, BorderLayout.EAST );


        label_destinationDirectory = new JLabel("Please choose the destination for the generated SpriteSheet:");
        textField_destinationDirectory = new JTextField();
        button_browse = new JButton("Browse");
        button_browse.addActionListener( this );

        panel_02_destinationDirectory = new JPanel( new BorderLayout(5,5));
        panel_02_destinationDirectory.add(label_destinationDirectory, BorderLayout.NORTH);
        JPanel panel_destinationPanel = new JPanel(new BorderLayout(5,5));
        panel_destinationPanel.add(textField_destinationDirectory, BorderLayout.CENTER);
        panel_destinationPanel.add(button_browse, BorderLayout.EAST);
        panel_02_destinationDirectory.add( panel_destinationPanel, BorderLayout.CENTER );


        label_isActive = new JLabel("Shall this directory be considered for Generation?");
        checkBox_isActive = new JCheckBox("Is Active:", true);

        panel_05_isActive = new JPanel( new BorderLayout(5,5));
        panel_05_isActive.add( label_isActive, BorderLayout.WEST );
        panel_05_isActive.add( checkBox_isActive, BorderLayout.EAST );


        button_next = new JButton("Next");
        button_next.addActionListener( this );
        button_previous = new JButton("Previous");
        button_previous.addActionListener( this );

        panel_06_buttons = new JPanel( new BorderLayout(5,5));
        panel_06_buttons.add( button_previous, BorderLayout.WEST );
        panel_06_buttons.add( button_next, BorderLayout.EAST );


        /** ---------------  final panels ------------------*/

        JPanel panel_00 = new JPanel( new BorderLayout(5,5));
        JPanel panel_01 = new JPanel( new BorderLayout(5,5));
        JPanel panel_02 = new JPanel( new BorderLayout(5,5));
        JPanel panel_03 = new JPanel( new BorderLayout(5,5));
        JPanel panel_04 = new JPanel( new BorderLayout(5,5));

        panel_00.add( panel_00_directory, BorderLayout.NORTH );
        panel_00.add( panel_03_information, BorderLayout.CENTER );
        panel_01.add( panel_00, BorderLayout.NORTH );
        panel_01.add( panel_04_customFilter, BorderLayout.CENTER );
        panel_02.add( panel_01, BorderLayout.NORTH );
        panel_02.add( panel_02_destinationDirectory, BorderLayout.CENTER );
        panel_03.add( panel_02, BorderLayout.NORTH );
        panel_03.add( panel_05_isActive, BorderLayout.CENTER );
        panel_04.add( panel_03, BorderLayout.NORTH );
        panel_04.add( panel_06_buttons, BorderLayout.SOUTH );


        this.add( panel_04 );

        setTitle("Settings for subDirectory");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }


    public void next( Node<File> node, boolean isLast ) {
        System.out.println( "Next called" );
        System.out.println("Node path: " + node.getData().getAbsolutePath() );
        this.setVisible( true );
        label_directory.setText("..." + node.getData().getAbsolutePath().substring(node.getData().getAbsolutePath().length() - 40));
        label_information_sprites.setText( "" + node.getFileAmount() );
        node.setCustomFilterValue( node.getFileAmount() );
        textField_customFilter.setText(""+node.getCustomFilterValue());
        if( isLast ) button_next.setText( "Generate" );
        else button_next.setText( "Next" );
        checkBox_isActive.setSelected( node.isActive() );
        textField_destinationDirectory.setText( node.getDestinationPath() );
        this.repaint();
    }





    @Override
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == button_browse ) {
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed( false );
            FileFilter imageFilter = new FileNameExtensionFilter( "PNG", "png" );
            fileChooser.addChoosableFileFilter( imageFilter );
            int retVal = fileChooser.showSaveDialog( this );
            if( retVal == JFileChooser.APPROVE_OPTION ) {
                File file = fileChooser.getSelectedFile();
                textField_destinationDirectory.setText( file.getAbsolutePath() );
            }
        }else if( e.getSource() == button_next ) {
            System.out.println( "Next clicked" );
            if( button_next.getText().equalsIgnoreCase("Generate")) {
                controller.finallyExport();
                this.setVisible( false );
            }else {
                if (checkBox_isActive.isSelected()) {
                    System.out.println( "isActive" );
                    if (textField_destinationDirectory.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                                "Please select an output path, if you want this directory to be considered for input.");
                    }
                    if (textField_customFilter.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                                "Please choose a filter value, if you want this directory to be considered for input.");
                    }
                    if (!textField_destinationDirectory.getText().isEmpty() && !textField_customFilter.getText().isEmpty()) {
                        File file = new File(textField_destinationDirectory.getText());
                        if (file != null) {
                            int n = JOptionPane.OK_OPTION;
                            if (file.exists()) {
                                n = JOptionPane.showConfirmDialog(this, "Output file does already exist. Overwrite?", "File already exists!", JOptionPane.OK_CANCEL_OPTION);
                            }
                            if (n == JOptionPane.OK_OPTION) {
                                //controller.saveSpritesheet( textField_destinationDirectory.getText() );
                                //TODO: save input to node
                                controller.getCurrentNode().setDestinationPath(textField_destinationDirectory.getText());
                                controller.getCurrentNode().setCustomFilterValue(Integer.parseInt(textField_customFilter.getText()));
                                controller.nextDirectory();
                            }
                        }
                    }
                } else {
                    System.out.println( "Not Active" );
                    controller.getCurrentNode().setActive(false);
                    controller.nextDirectory();
                }
            }
        }else if( e.getSource() == button_previous ) {
            controller.previousDirectory();
        }
    }
}
