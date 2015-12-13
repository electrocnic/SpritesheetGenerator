import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by Andreas on 13.12.2015.
 */
public class SGView extends JFrame implements ActionListener {

    public static final int WIDTH = 500;
    public static final int HEIGHT = 400;

    private SpritesheetGenerator controller = null;

    private JFileChooser fileChooser = null;

    private JLabel label_importDirectory = null;
    private JTextField textField_importDirectory = null;
    private JButton button_importDirectory = null;
    private JPanel panel_importDirectory = null;

    private JLabel label_exportFile = null;
    private JTextField textField_exportFile = null;
    private JButton button_exportFile = null;
    private JPanel panel_exportFile = null;

    private JButton button_load = null;
    private JLabel label_status_title = null;
    private JLabel label_width = null;
    private JLabel label_width_val = null;
    private JLabel label_height = null;
    private JLabel label_height_val = null;
    private JLabel label_total_width = null;
    private JLabel label_total_width_val = null;
    private JLabel label_image_count = null;
    private JLabel label_image_count_val = null;
    private JLabel label_total_file_size = null;
    private JLabel label_total_file_size_val = null;

    private JButton button_start = null;


    private JLabel label_image_preview = null;


    public SGView( SpritesheetGenerator controller ) {
        this.controller = controller;

        setResizable(false);
        setSize(WIDTH, HEIGHT);




        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory( new File( "E:\\Projects\\sketches\\flea_new\\png\\final") );

        /** Import Directory **/

        label_importDirectory = new JLabel("Source Directory");
        Font font = label_importDirectory.getFont();
        Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
        label_importDirectory.setFont(boldFont);

        textField_importDirectory = new JTextField();
        button_importDirectory = new JButton("Browse");
        button_importDirectory.addActionListener(this);
        JPanel panel_importDirectory_fileChooser = new JPanel( new BorderLayout(5,5));

        panel_importDirectory = new JPanel( new BorderLayout(5,5) );

        panel_importDirectory_fileChooser.add(textField_importDirectory, BorderLayout.CENTER);
        panel_importDirectory_fileChooser.add(button_importDirectory, BorderLayout.EAST);

        panel_importDirectory.add(label_importDirectory, BorderLayout.NORTH);
        panel_importDirectory.add( panel_importDirectory_fileChooser, BorderLayout.CENTER );

        JPanel panel_import = new JPanel( new BorderLayout(5,5));
        panel_import.add(panel_importDirectory, BorderLayout.NORTH);


        /** Export File **/
        label_exportFile = new JLabel("Destination");
        font = label_exportFile.getFont();
        boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
        label_exportFile.setFont(boldFont);

        textField_exportFile = new JTextField();
        button_exportFile = new JButton("Browse");
        button_exportFile.addActionListener(this);
        JPanel panel_exportFile_fileChooser = new JPanel( new BorderLayout(5,5) );

        panel_exportFile = new JPanel( new BorderLayout(5,5));

        panel_exportFile_fileChooser.add(textField_exportFile, BorderLayout.CENTER);
        panel_exportFile_fileChooser.add(button_exportFile, BorderLayout.EAST);

        panel_exportFile.add(label_exportFile, BorderLayout.NORTH);
        panel_exportFile.add( panel_exportFile_fileChooser, BorderLayout.CENTER );

        JPanel panel_export = new JPanel( new BorderLayout(5,5) );
        panel_export.add(panel_exportFile, BorderLayout.NORTH);


        /** Status **/

        button_load = new JButton("Load");
        button_load.addActionListener(this);
        button_load.setHorizontalAlignment(SwingConstants.CENTER);

        label_status_title = new JLabel("--- Status ---");
        font = label_status_title.getFont();
        boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
        label_status_title.setFont(boldFont);

        JPanel panel_imageCount = new JPanel( new BorderLayout());
        label_image_count = new JLabel("Amount of Sprites loaded: ");
        label_image_count_val = new JLabel();
        panel_imageCount.add( label_image_count, BorderLayout.WEST );
        panel_imageCount.add( label_image_count_val, BorderLayout.EAST );

        JPanel panel_width = new JPanel(new BorderLayout());
        label_width = new JLabel("Width per Sprite: ");
        label_width_val = new JLabel();
        panel_width.add( label_width, BorderLayout.WEST );
        panel_width.add( label_width_val, BorderLayout.EAST );

        JPanel panel_height = new JPanel(new BorderLayout());
        label_height = new JLabel("Height per Sprite: ");
        label_height_val = new JLabel();
        panel_height.add( label_height, BorderLayout.WEST );
        panel_height.add( label_height_val, BorderLayout.EAST );

        JPanel panel_totalWidth = new JPanel(new BorderLayout());
        label_total_width = new JLabel("Total width: ");
        label_total_width_val = new JLabel();
        panel_totalWidth.add( label_total_width, BorderLayout.WEST );
        panel_totalWidth.add( label_total_width_val, BorderLayout.EAST );

        JPanel panel_totalFileSize = new JPanel(new BorderLayout());
        label_total_file_size = new JLabel("Total file size: ");
        label_total_file_size_val = new JLabel();
        panel_totalFileSize.add( label_total_file_size, BorderLayout.WEST );
        panel_totalFileSize.add( label_total_file_size_val, BorderLayout.EAST );

        button_start = new JButton("Render");
        button_start.addActionListener(this);
        button_start.setHorizontalAlignment(SwingConstants.CENTER);

        /** Layout for Status **/

        JPanel panel_loadButton_statusTitle = new JPanel( new BorderLayout(5,5));
        panel_loadButton_statusTitle.add( button_load, BorderLayout.NORTH );
        panel_loadButton_statusTitle.add( label_status_title, BorderLayout.CENTER );

        JPanel panel_2_ImageCount = new JPanel( new BorderLayout(5,5));
        panel_2_ImageCount.add( panel_loadButton_statusTitle, BorderLayout.NORTH );
        panel_2_ImageCount.add( panel_imageCount );

        JPanel panel_3_width = new JPanel( new BorderLayout(5,5));
        panel_3_width.add( panel_2_ImageCount, BorderLayout.NORTH );
        panel_3_width.add( panel_width, BorderLayout.CENTER );

        JPanel panel_4_height = new JPanel( new BorderLayout(5,5));
        panel_4_height.add( panel_3_width, BorderLayout.NORTH );
        panel_4_height.add( panel_height, BorderLayout.CENTER );

        JPanel panel_5_totalWidth = new JPanel( new BorderLayout(5,5));
        panel_5_totalWidth.add( panel_4_height, BorderLayout.NORTH );
        panel_5_totalWidth.add( panel_totalWidth, BorderLayout.CENTER );

        JPanel panel_6_totalFileSize = new JPanel( new BorderLayout(5,5));
        panel_6_totalFileSize.add( panel_5_totalWidth, BorderLayout.NORTH );
        panel_6_totalFileSize.add( panel_totalFileSize, BorderLayout.CENTER );

        JPanel panel_7_startButton = new JPanel( new BorderLayout(5,5));
        panel_7_startButton.add( panel_6_totalFileSize, BorderLayout.NORTH );
        panel_7_startButton.add( button_start, BorderLayout.CENTER );


        /** Image Preview **/
        label_image_preview = new JLabel();



        /** Finally **/


        JPanel panel_directories = new JPanel( new BorderLayout(5,5));
        panel_directories.add( panel_import, BorderLayout.NORTH );
        panel_directories.add(panel_export, BorderLayout.CENTER);

        JPanel directories_statusField = new JPanel( new BorderLayout(5,5));
        directories_statusField.add( panel_directories, BorderLayout.NORTH );
        directories_statusField.add(panel_7_startButton, BorderLayout.CENTER);

        JPanel last_imageprev = new JPanel( new BorderLayout(5,5));
        last_imageprev.add( directories_statusField, BorderLayout.NORTH );
        last_imageprev.add( label_image_preview, BorderLayout.CENTER );

        this.add(last_imageprev);




        //pack();
        setTitle("SpritesheetGenerator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == button_importDirectory ) {
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int retVal = fileChooser.showOpenDialog( this );
            if( retVal == JFileChooser.APPROVE_OPTION ) {
                File file = fileChooser.getSelectedFile();
                textField_importDirectory.setText( file.getAbsolutePath() );
            }
        }else if( e.getSource() == button_exportFile ) {
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed( false );
            FileFilter imageFilter = new FileNameExtensionFilter( "PNG", "png" );
            fileChooser.addChoosableFileFilter( imageFilter );
            int retVal = fileChooser.showSaveDialog( this );
            if( retVal == JFileChooser.APPROVE_OPTION ) {
                File file = fileChooser.getSelectedFile();
                textField_exportFile.setText( file.getAbsolutePath() );
            }
        }else if( e.getSource() == button_load ) {
            String importDirectory = textField_importDirectory.getText();
            if( importDirectory!=null && !importDirectory.isEmpty() ) {
                File file = new File( importDirectory );
                if( file != null && file.exists() ) {
                    if( file.isDirectory() ) {
                        controller.loadSprites(importDirectory);
                    }
                }else {
                    JOptionPane.showMessageDialog( this,
                            "Invalid directory path. Please choose an existing directory.");
                }
            }else {
                JOptionPane.showMessageDialog( this,
                        "No source path chosen yet!" );
            }
        }else if( e.getSource() == button_start ) {
            String exportFilePath = textField_exportFile.getText();
            if( exportFilePath!=null && !exportFilePath.isEmpty() ) {
                File file = new File( exportFilePath );
                if( file!=null ) {
                    int n = JOptionPane.OK_OPTION;
                    if( file.exists() ) {
                        n = JOptionPane.showConfirmDialog( this, "Output file does already exist. Overwrite?", "File already exists!", JOptionPane.OK_CANCEL_OPTION );
                    }
                    if( n==JOptionPane.OK_OPTION ) {
                        controller.saveSpritesheet( exportFilePath );
                    }
                }
            }else {
                JOptionPane.showMessageDialog( this,
                        "No destination path chosen yet!" );
            }
        }
    }

    public void setImageLabel( BufferedImage image ) {
        label_image_preview.setIcon( new ImageIcon(image));
        this.repaint();
    }

    public void reset() {
        label_image_count_val.setText("");
        label_height_val.setText("");
        label_width_val.setText("");
        label_total_width_val.setText("");
        label_total_file_size_val.setText("");
    }

    public void setSpriteAmount(int length) {
        label_image_count_val.setText( ""+length );
        this.repaint();
    }

    public void setSpriteWidth(int width) {
        label_width_val.setText("" + width);
        this.repaint();
    }


    public void setSpriteHeight(int height) {
        label_height_val.setText(""+height);
        this.repaint();
    }

    public void setTotalWidth(int i) {
        label_total_width_val.setText(""+i);
        this.repaint();
    }

    public void setTotalFileSize(int fileSize) {
        label_total_file_size_val.setText(""+fileSize+" kB");
        this.repaint();
    }
}
