import sun.rmi.runtime.Log;

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

    FileFilter filter_png = new FileFilter() {
        @Override
        public boolean accept(File pathname) {return (pathname.isFile() && pathname.getName().endsWith("png"));
        }
    };
    FileFilter filter_directory = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return pathname.isDirectory();
        }
    };


    public void loadSprites( String importDirectory ) {
        model.reset();
        view.reset();

        Node<File> tree = loadSprites( new File( importDirectory ));
        System.out.println( "finished" );
        model.setTree( tree );

        //TODO: make GUI. (vorher: make input dialogs at render. with filechoosers...)

    }

    /**
     * Creates a new SubTree which consists of all Images and subdirectories within the given directory.
     * @param directory
     * @return
     */
    public Node<File> loadSprites( File directory ) {
        Node<File> subTree=null;
        subTree = loadImages( subTree, directory );

        System.out.println(".");

        File[] directories = directory.listFiles( filter_directory );
        int i=0;
        for( File subDirectory : directories ) {
            System.out.println( i );
            subTree.addDirectory(loadSprites(subDirectory));
            i++;
        }
        return subTree;
    }

    /**
     * Loads the bufferedImages in the given directory to a new Tree. The head of the tree is a Node with type = directory
     * which contains images, which are also nodes, but from the type "file".
     * @param path The path of the directory, where the images are located.
     * @return A new tree with the loaded images.
     */
    public Node<File> loadImages( String path ) {
        return loadImages( null, path );
    }

    public Node<File> loadImages( Node<File> tree, String path ) {
        File directory = new File( path );
        return loadImages( tree, directory );
    }

    /**
     * Loads the bufferedImages in the given directory to a new Tree. The head of the tree is a Node with type = directory
     * which contains images, which are also nodes, but from the type "file".
     * @param tree The Node where the images should be added.
     * @param directory The file of the directory, where the images are located.
     * @return The tree with the images added.
     */
    public Node<File> loadImages( Node<File> tree, File directory ) {

        if( tree==null ) tree = new Node<File>( null, directory, Node.FileType.DIRECTORY );
        File[] files = directory.listFiles( filter_png );
        int fileSize = 0;
        if( files!= null && files.length!=0 ) {
            List<String> filenames = getSortedFileNames( files );

            for( int u=0; u<filenames.size(); u++) {
                String name = filenames.get(u);
                File file = new File( directory.getAbsolutePath() + File.separator + name );
                fileSize += file.length();
                BufferedImage img = null;
                try{
                    img = ImageIO.read( file );
                }catch ( IOException e ) {
                }

                if( img != null ) {
                    tree.addFile(new Node<BufferedImage>(img, Node.FileType.FILE));

                    model.addSprite( img );
                    model.addHeight( img.getHeight() );
                    model.addWidth( img.getWidth() );
                }else if( u<=3 ) {
                    JOptionPane.showMessageDialog( view, "img==null :(  ... File: " + file.getName() + ";  exists=" + file.exists() + ";  importDirectory: " + directory.getAbsolutePath() );
                }
            }

            /**
            if( !model.heightsAreEqual() ) {
                JOptionPane.showMessageDialog(view, "The images are not equal in height.");
                model.reset();
                view.reset();
            }else {
                if( !model.widthsAreEqual() )
                    JOptionPane.showMessageDialog( view, "The images are not equal in width. The spritesheet WILL be generated, but better check, if all is right!");
                /*
                view.setSpriteAmount( model.getSprites().size() );
                view.setSpriteWidth(model.getSprites().get(0).getWidth());
                view.setSpriteHeight(model.getSprites().get(0).getHeight());
                view.setTotalWidth(model.getSprites().size() * model.getSprites().get(0).getWidth());
                view.setTotalFileSize(fileSize / 1024);
                view.setImageLabel( model.getSprites().get(0));
                *
                //TODO: set above information for nodes not for view.
            } */

        }else { //just no "files" in this directory, but maybe in a subdirectory.
            //JOptionPane.showMessageDialog(view, "No sprites (png files) detected!");
        }

        return tree;
    }

    /**
     * Filters the images from the models nodes and returns a new Tree.
     * @param tree
     * @return
     */
    public Node<File> filterImages( Node<File> tree ) {
        return filterImages( tree, null );
    }

    /**
     * Filters the images from the models nodes and puts them into the new Tree.
     * @param tree
     * @param newTree
     * @return
     */
    public Node<File> filterImages( Node<File> tree, Node<File> newTree ) {
        int u=0;
        float nte = 1; //jedes nte element wird ausgewaehlt
        float startOffset = 0; //offset für gleichmäßige verteilung
        float customIndex = 0;
        if( newTree == null ) newTree = new Node<File>(null, tree.getData(), Node.FileType.DIRECTORY);

        if( model.getFfstate()== SGModel.FileFilterState.ODD ) u++;
        else if( model.getFfstate() == SGModel.FileFilterState.CUSTOM )  {
            nte = (float)tree.getFileAmount() / (float)model.getCustomFilterValue();
            startOffset = (nte-1)/2;
            customIndex += startOffset;
        }

        for( ; u<tree.getFileAmount(); ) {

            newTree.addFile(new Node<BufferedImage>((BufferedImage) tree.getFileAt(u).getData(), Node.FileType.FILE));

            switch ( model.getFfstate() ) {
                case ALL:
                    u++;
                    break;
                case EVEN:
                case ODD:
                    u+=2;
                    break;
                case CUSTOM:
                    customIndex += nte;
                    u = (int) customIndex;
                    break;
            }
        }

        //TODO: set for each Node
        /*
        view.setSpriteAmount( model.getSprites().size() );
        view.setSpriteWidth(model.getSprites().get(0).getWidth());
        view.setSpriteHeight(model.getSprites().get(0).getHeight());
        view.setTotalWidth(model.getSprites().size() * model.getSprites().get(0).getWidth());
        view.setTotalFileSize(newTree.getTotalFileSize() / 1024);
        view.setImageLabel( model.getSprites().get(0));
        */

        return newTree;
    }


    public Node<File> loadDirectories( String path ) {
        return loadDirectories( null, path );
    }

    public Node<File> loadDirectories( Node<File> tree, String path ) {
        File directory = new File( path );
        if( tree==null ) tree = new Node<File>( null, directory, Node.FileType.DIRECTORY );
        File[] files = directory.listFiles( filter_directory );

        if( files!= null && files.length!=0 ) {

        }
        return tree;
    }

    public List<String> getSortedFileNames( File[] files ) {
        List<String> filenames = new ArrayList<>();
        for( File file : files ) {
            filenames.add( file.getName() );
        }

        Collections.sort( filenames );

        return filenames;
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
                    finalSpriteSheet.createGraphics().drawImage(image, model.getWidthTo(i), 0, null);
                }
                view.setImageLabel(finalSpriteSheet);

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

    public int nextState() {
        return model.nextState();
    }

    public void setGlobalCustomFilter( int value ) {
        model.setCustomFilterValue( value );
    }

    public SGModel.FileFilterState getFfState() {
        return model.getFfstate();
    }

    public String getCustomFilterValue() {
        return ""+model.getCustomFilterValue();
    }
}
