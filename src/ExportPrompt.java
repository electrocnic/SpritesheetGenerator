import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * Created by Andreas on 01.01.2016.
 */
public class ExportPrompt extends JPanel implements ActionListener{

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
    private JLabel label_firstAlwaysActive = null;
    private JLabel label_lastAlwaysActive = null;
    private JCheckBox checkBox_firstAlwaysActive = null;
    private JCheckBox checkBox_lastAlwaysActive = null;
    private JLabel label_information_sprite_height_title = null;
    private JLabel label_information_sprite_height = null;
    private JLabel label_information_sprite_totalwidth_title = null;
    private JLabel label_information_sprite_totalwidth = null;
    private JLabel label_information_sprite_totalwidth_customFilter_title = null;
    private JLabel label_information_sprite_totalwidth_customFilter = null;
    private JLabel label_information_customFilterIndizes_title = null;
    private JLabel label_information_customFilterIndizes = null;
    private JButton button_browse = null;
    private JLabel label_destinationDirectory = null;
    private JTextField textField_destinationDirectory = null;
    private JCheckBox checkBox_isActive = null;
    private JLabel label_isActive = null;


    //private JButton button_next = null;
    //private JButton button_previous = null;

    private JPanel panel_00_directory = null;
    private JPanel panel_02_destinationDirectory = null;
    private JPanel panel_03_information = null;
    private JPanel panel_04_customFilter = null;
    private JPanel panel_05_isActive = null;
    //private JPanel panel_06_buttons = null;


    public ExportPrompt( SpritesheetGenerator controller, JFrame parent ) {
        this.controller = controller;
        //setResizable(false);
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


        label_firstAlwaysActive = new JLabel("First frame ALWAYS active? (Independent from Custom Filter value)");
        checkBox_firstAlwaysActive = new JCheckBox("Is Active", true);
        checkBox_firstAlwaysActive.addActionListener(this);

        JPanel panel_firstAlwaysActive = new JPanel( new BorderLayout(5,5));
        panel_firstAlwaysActive.add( label_firstAlwaysActive, BorderLayout.WEST );
        panel_firstAlwaysActive.add(checkBox_firstAlwaysActive, BorderLayout.EAST);


        label_customFilter = new JLabel("Custom Filter for this Directory:");
        textField_customFilter = new JIntegerField("");
        textField_customFilter.setPreferredSize(new Dimension(35, 25));
        textField_customFilter.setActionListener(this);

        panel_04_customFilter = new JPanel( new BorderLayout(5,5));
        panel_04_customFilter.add(label_customFilter, BorderLayout.WEST);
        panel_04_customFilter.add( textField_customFilter, BorderLayout.EAST );


        label_lastAlwaysActive = new JLabel("Last frame ALWAYS active?");
        checkBox_lastAlwaysActive = new JCheckBox("Is Active", true);
        checkBox_lastAlwaysActive.addActionListener(this);

        JPanel panel_lastAlwaysActive = new JPanel( new BorderLayout(5,5));
        panel_lastAlwaysActive.add( label_lastAlwaysActive, BorderLayout.WEST );
        panel_lastAlwaysActive.add(checkBox_lastAlwaysActive, BorderLayout.EAST);


        label_destinationDirectory = new JLabel("Please choose the destination for the generated SpriteSheet:");
        textField_destinationDirectory = new JTextField();
        //textField_destinationDirectory.setMaximumSize( new Dimension(500, 35));
        //textField_destinationDirectory.setSize( textField_destinationDirectory.getWidth(), textField_destinationDirectory.getHeight() );
        textField_destinationDirectory.setColumns(10);
        button_browse = new JButton("Browse");
        button_browse.addActionListener(this);

        panel_02_destinationDirectory = new JPanel( new BorderLayout(5,5));
        panel_02_destinationDirectory.add(label_destinationDirectory, BorderLayout.NORTH);
        JPanel panel_destinationPanel = new JPanel(new BorderLayout(5,5));
        panel_destinationPanel.add(textField_destinationDirectory, BorderLayout.CENTER);
        panel_destinationPanel.add(button_browse, BorderLayout.EAST);
        panel_02_destinationDirectory.add( panel_destinationPanel, BorderLayout.CENTER );


        label_isActive = new JLabel("Shall this directory be considered for Generation?");
        checkBox_isActive = new JCheckBox("Is Active", true);

        panel_05_isActive = new JPanel( new BorderLayout(5,5));
        panel_05_isActive.add( label_isActive, BorderLayout.WEST );
        panel_05_isActive.add( checkBox_isActive, BorderLayout.EAST );


        label_information_sprite_height_title = new JLabel("Height of first sprite:");
        label_information_sprite_height = new JLabel("");
        JPanel panel_height = new JPanel( new BorderLayout(5,5) );
        panel_height.add( label_information_sprite_height_title, BorderLayout.WEST );
        panel_height.add( label_information_sprite_height, BorderLayout.EAST );


        label_information_sprite_totalwidth_title = new JLabel("Total width of all sprites in this directory:");
        label_information_sprite_totalwidth = new JLabel("");
        JPanel panel_totalWidth = new JPanel(new BorderLayout(5,5));
        panel_totalWidth.add( label_information_sprite_totalwidth_title, BorderLayout.WEST );
        panel_totalWidth.add( label_information_sprite_totalwidth, BorderLayout.EAST );


        label_information_sprite_totalwidth_customFilter_title = new JLabel("Total width of filtered sprites:");
        label_information_sprite_totalwidth_customFilter = new JLabel("");
        JPanel panel_totalWidthCustom = new JPanel(new BorderLayout(5,5));
        panel_totalWidthCustom.add( label_information_sprite_totalwidth_customFilter_title, BorderLayout.WEST );
        panel_totalWidthCustom.add( label_information_sprite_totalwidth_customFilter, BorderLayout.EAST );


        label_information_customFilterIndizes_title = new JLabel("Selected images:");
        label_information_customFilterIndizes = new JLabel("");
        JPanel panel_indizes = new JPanel(new BorderLayout(5,5));
        panel_indizes.add(label_information_customFilterIndizes_title, BorderLayout.WEST);
        JScrollPane scrollPane = new JScrollPane(label_information_customFilterIndizes);
        Dimension scrollsize = new Dimension(450, 40);
        scrollPane.setMaximumSize( scrollsize );
        scrollPane.setPreferredSize( scrollsize);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JPanel panelScroll = new JPanel();
        panelScroll.add( scrollPane );
        panelScroll.setMaximumSize(scrollsize);
        panelScroll.setSize(scrollsize);
        panel_indizes.add( panelScroll, BorderLayout.EAST );

/*
        button_next = new JButton("Next");
        button_next.addActionListener( this );
        button_previous = new JButton("Previous");
        button_previous.addActionListener( this );

        panel_06_buttons = new JPanel( new BorderLayout(5,5));
        panel_06_buttons.add( button_previous, BorderLayout.WEST );
        panel_06_buttons.add( button_next, BorderLayout.EAST );*/


        /** ---------------  final panels ------------------*/

        JPanel panel_00 = new JPanel( new BorderLayout(5,5));
        JPanel panel_01 = new JPanel( new BorderLayout(5,5));
        JPanel panel_02 = new JPanel( new BorderLayout(5,5));
        JPanel panel_03 = new JPanel( new BorderLayout(5,5));
        JPanel panel_04 = new JPanel( new BorderLayout(5,5));
        JPanel panel_05 = new JPanel( new BorderLayout(5,5));
        JPanel panel_06 = new JPanel( new BorderLayout(5,5));
        JPanel panel_07 = new JPanel( new BorderLayout(5,5));
        JPanel panel_08 = new JPanel( new BorderLayout(5,5));
        JPanel panel_09 = new JPanel( new BorderLayout(5,5));
        //JPanel panel_10 = new JPanel( new BorderLayout(5,5));

        panel_00.add( panel_00_directory, BorderLayout.NORTH );
        panel_00.add( panel_03_information, BorderLayout.CENTER );
        panel_01.add( panel_00, BorderLayout.NORTH );
        panel_01.add( panel_firstAlwaysActive, BorderLayout.CENTER );
        panel_02.add( panel_01, BorderLayout.NORTH );
        panel_02.add( panel_04_customFilter, BorderLayout.CENTER );
        panel_03.add( panel_02, BorderLayout.NORTH );
        panel_03.add( panel_lastAlwaysActive, BorderLayout.CENTER );
        panel_04.add( panel_03, BorderLayout.NORTH );
        panel_04.add( panel_02_destinationDirectory, BorderLayout.CENTER );
        panel_05.add( panel_04, BorderLayout.NORTH );
        panel_05.add( panel_05_isActive, BorderLayout.CENTER );
        panel_06.add( panel_05, BorderLayout.NORTH );
        panel_06.add( panel_height, BorderLayout.CENTER );
        panel_07.add( panel_06, BorderLayout.NORTH );
        panel_07.add( panel_totalWidth, BorderLayout.CENTER );
        panel_08.add( panel_07, BorderLayout.NORTH );
        panel_08.add( panel_totalWidthCustom, BorderLayout.CENTER );
        panel_09.add( panel_08, BorderLayout.NORTH );
        panel_09.add( panel_indizes, BorderLayout.CENTER );
        //panel_10.add( panel_09, BorderLayout.NORTH );
        //panel_10.add(panel_06_buttons, BorderLayout.SOUTH);

        //panel_10.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.add(panel_09);

        WindowAdapter windowAdapter = new WindowAdapter()
        {
            public void windowClosing(WindowEvent we)
            {
                //setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                controller.resetCurrentNodeIndex();
                parent.setFocusable(true);
            }
        };

        //addWindowListener(windowAdapter);

        //setTitle("Settings for subDirectory");
        //setDefaultCloseOperation(HIDE_ON_CLOSE);
        //this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }


    public void next( Node<File> node, boolean isLast ) {
        //System.out.println("Next called");
        //System.out.println("Node path: " + node.getData().getAbsolutePath());
        if( node != null ) {
            this.setVisible(true);
            int length = node.getData().getAbsolutePath().length() - 70;
            if (length < 0) length = 0;
            label_directory.setText("..." + node.getData().getAbsolutePath().substring(length));
            label_information_sprites.setText("" + node.getFileAmount());
            if (!node.isCustomFilterChanged() && (controller.getCustomFilterValue().equalsIgnoreCase("0") || Integer.parseInt(controller.getCustomFilterValue()) > node.getFileAmount())) {
                node.setCustomFilterValue(node.getFileAmount());
                textField_customFilter.setText("" + node.getFileAmount());
            } else if (!node.isCustomFilterChanged()) {
                node.setCustomFilterValue(Integer.parseInt(controller.getCustomFilterValue()));
                textField_customFilter.setText("" + controller.getCustomFilterValue());
            } else textField_customFilter.setText("" + node.getCustomFilterValue());

            checkBox_isActive.setSelected(node.isActive());
            String path = "";
            if( !node.isCustomDirectoryChanged() && !controller.getGlobalExportPath().isEmpty() ) {
                //autoname: export global given. custom did not change yet.
                path = controller.getGlobalExportPath()+File.separator;
                path+=controller.getCurrentIndex();
                path+="_spritesheet_";
                if( node.getParent() != null ) path += node.getParent().getData().getName();
                node.setDestinationPath( path );
            }
            textField_destinationDirectory.setText(node.getDestinationPath());
            checkBox_firstAlwaysActive.setSelected(node.firstAlwaysActive());
            checkBox_lastAlwaysActive.setSelected(node.lastAlwaysActive());
            label_information_sprite_height.setText("" + node.getHeight());
            label_information_sprite_totalwidth.setText("" + node.getTotalWidth());
            setCustomWidth(node);
        }
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
        }else if( e.getSource() == textField_customFilter || e.getSource() == checkBox_firstAlwaysActive || e.getSource() == checkBox_lastAlwaysActive ) {
            if (e.getSource() == textField_customFilter) {
                if( textField_customFilter.getText().isEmpty() ) {
                    controller.getCurrentNode().setCustomFilterChanged(false);
                }else controller.getCurrentNode().setCustomFilterChanged(true);
            }
            controller.getCurrentNode().setCustomFilterValue(Integer.parseInt(textField_customFilter.getText()));
            controller.getCurrentNode().setFirstAlwaysActive(checkBox_firstAlwaysActive.isSelected());
            controller.getCurrentNode().setLastAlwaysActive(checkBox_lastAlwaysActive.isSelected());
            setCustomWidth(controller.getCurrentNode());
        }else if( e.getSource() == textField_destinationDirectory ) {
            if( textField_destinationDirectory.getText().isEmpty() ) {
                controller.getCurrentNode().setCustomDirectoryChanged(false);
            }else {
                controller.getCurrentNode().setDestinationPath(textField_destinationDirectory.getText());
                controller.getCurrentNode().setCustomDirectoryChanged(true);
            }
        }
    }

    public boolean nextPressed() {
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

                        controller.nextDirectory();
                        return true;
                    }
                }
            }
        } else {
            System.out.println("Not Active");
            controller.getCurrentNode().setActive(false);
            controller.nextDirectory();
            return true;
        }
        return false;
    }

    public void updateNode() {
        controller.getCurrentNode().setDestinationPath(textField_destinationDirectory.getText());
        controller.getCurrentNode().setCustomFilterValue(Integer.parseInt(textField_customFilter.getText()));
        controller.getCurrentNode().setActive(checkBox_isActive.isSelected());
        controller.getCurrentNode().setFirstAlwaysActive(checkBox_firstAlwaysActive.isSelected());
        controller.getCurrentNode().setLastAlwaysActive(checkBox_lastAlwaysActive.isSelected());
    }

    public void setCustomWidth( Node<File> node ) {
        label_information_sprite_totalwidth_customFilter.setText("" + node.getCustomWidth());
        String indizes = "";
        for( int i : node.getCustomIndizes() ) {
            indizes += i + ", ";
        }
        if( indizes.isEmpty() ) indizes="  ";
        label_information_customFilterIndizes.setText(indizes.substring(0, indizes.length() - 2));
    }
}
