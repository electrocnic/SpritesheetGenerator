import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Main Entry Point
 * Created by Andreas on 13.12.2015.
 */
public class SpritesheetGenerator {

    private SGView view = null;
    private SGModel model = null;

    public SpritesheetGenerator() {
        model = new SGModel( this );
        view = new SGView( this );
    }

    public void loadSprites( String importDirectory ) {

        model.reset();
        view.reset();

        File importDirectoryFile = new File( importDirectory );
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if( pathname.isFile() && pathname.getName().endsWith("png")) return true;
                return false;
            }
        };
        File[] files = importDirectoryFile.listFiles( filter );
        int fileSize = 0;
        if( files!= null && files.length!=0 ) {
            List<String> filenames = new ArrayList<>();
            for( File file : files ) {
                fileSize += file.length();
                filenames.add( file.getName() );
            }

            Collections.sort( filenames );

            JOptionPane.showMessageDialog( view, "Order of Elements: " + filenames.get(0) + "; " + filenames.get(1) + "; " + filenames.get(2) + "; " + filenames.get(3) + "; ...");

            int u=0;
            for( String name : filenames ) {
                File file = new File( importDirectory + File.separator + name );
                BufferedImage img = null;
                try{
                    img = ImageIO.read( file );
                }catch ( IOException e ) {
                }

                if( img != null ) {
                    model.addSprite( img );
                    model.addHeight( img.getHeight() );
                    model.addWidth( img.getWidth() );
                }else if( u<=3 ) {
                    JOptionPane.showMessageDialog( view, "img==null :(  ... File: " + file.getName() + ";  exists=" + file.exists() + ";  importDirectory: " + importDirectory );
                }
                u++;
            }

            if( model.getSprites().size() != filenames.size() ) {
                JOptionPane.showMessageDialog(view, "An Error occured while loading the sprites: Model: " + model.getSprites().size() + ";  filenames: " +  filenames.size());
                model.reset();
                view.reset();
            }else if( !model.heightsAreEqual() ) {
                JOptionPane.showMessageDialog( view, "The images are not equal in height.");
                model.reset();
                view.reset();
            }else if( !model.widthsAreEqual() ) {
                JOptionPane.showMessageDialog( view, "The images are not equal in width.");
                model.reset();
                view.reset();
            }else {
                view.setSpriteAmount( model.getSprites().size() );
                view.setSpriteWidth(model.getSprites().get(0).getWidth());
                view.setSpriteHeight(model.getSprites().get(0).getHeight());
                view.setTotalWidth(model.getSprites().size() * model.getSprites().get(0).getWidth());
                view.setTotalFileSize(fileSize / 1024);
                view.setImageLabel( model.getSprites().get(0));
            }

        }else {
            JOptionPane.showMessageDialog(view, "No sprites (png files) detected!");
        }
    }

    public void saveSpritesheet( String exportPath ) {
        if( model.hasSprites() ) {
            int height = model.getHeight();
            int width = model.getWidth();
            int totalWidth = model.getTotalWidth();
            if( height != -1 && width != -1 && totalWidth != -1 ) {
                BufferedImage finalSpriteSheet = new BufferedImage(totalWidth, height, BufferedImage.TYPE_INT_ARGB_PRE);
                //JOptionPane.showMessageDialog( view, "totalWidth = " + totalWidth + ";  height = " + height + ";  type = " + model.getType() );
                //JOptionPane.showMessageDialog( view, "Amount of pics: " + model.getSprites().size() +";  finalSpriteSheet Width and height: "
                //        + finalSpriteSheet.getWidth() +", " +finalSpriteSheet.getHeight());
                for (int i = 0; i < model.getSprites().size(); i++) {
                    BufferedImage image = model.getSprites().get(i);
                    finalSpriteSheet.createGraphics().drawImage(image, width * i, 0, null);
                }
                view.setImageLabel( finalSpriteSheet );

                if( !exportPath.endsWith("png")) exportPath+=".png";

                try {
                    ImageIO.write( finalSpriteSheet, "png", new File(exportPath) );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                JOptionPane.showMessageDialog( view, "An error occured: One of the sizes is -1");
            }

        }else {
            JOptionPane.showMessageDialog( view, "No sprites loaded yet!" );
        }
    }

    public static void main(String[] args) {
        SpritesheetGenerator sg = new SpritesheetGenerator();
    }
}
