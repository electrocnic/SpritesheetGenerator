import javafx.beans.binding.StringExpression;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
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
    private SettingsDialog dialog = null;

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


    public void loadDirectories(String importDirectory) {
        model.reset();
        view.reset();

        Node<File> tree = new Node<File>( new File(importDirectory), Node.FileType.DIRECTORY );
        tree = loadDirectories( tree, 0); //directories loaded.
        model.addDirectory( tree );
        loadSprites( null ); //null will remain datastructure as is
        //"E:\\Projects\\sketches\\flea_new\\png\\final\\hdpi"
        //System.out.println( "finished" );
        model.setTree( tree );

        //TODO: make GUI. (vorher: make input dialogs at render. with filechoosers...)

    }

    /**
     * Creates a new SubTree which consists of all Images and subdirectories within the given directory.
     * @param node
     * @return
     */
    public Node<File> loadDirectories(Node<File> node, int level) {
        if( node == null ) return null;

        System.out.println("Level: " + level);

        File[] images = node.getData().listFiles(filter_png); //check if this directory is empty
        File[] directories = node.getData().listFiles(filter_directory);

        if( images.length==0 && directories.length==0 ) return null;
        int i=0;
        int anzNull = 0;
        for( File subDirectory : directories ) {
            System.out.println(i);
            Node<File> tmp = new Node<File>( subDirectory, Node.FileType.DIRECTORY );
            tmp = loadDirectories(tmp, level + 1);
            if( tmp == null ) anzNull++;
            node.addDirectory( tmp );
            model.addDirectory( tmp );
            i++;
        }
        if( anzNull == i && images!=null && images.length == 0 ) return null; //this excludes empty directories, even if they have empty subdirectories.
        return node;
    }

    /**
     * Loads the bufferedImages in the given directory to a new Tree. The head of the tree is a Node with type = directory
     * which contains images, which are also nodes, but from the type "file".
     * @param path The path of the directory, where the images are located.
     * @return A new tree with the loaded images.
     */
    public Node<File> loadImages( String path ) {
        return loadImages(null, path);
    }

    /**
     * Loads the images in the directory from the tree. Saves the images to this tree.
     * @param tree
     * @return
     */
    public Node<File> loadImages( Node<File> tree ) {
        return loadImages( tree, tree.getData() );
    }

    public Node<File> loadImages( Node<File> tree, String path ) {
        File directory = new File( path );
        return loadImages(tree, directory);
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
    public Node<File> filterImages( Node<File> tree) {
        return filterImages(tree, null);
    }

    /**
     * Filters the images from the models nodes and puts them into the new Tree.
     * @param tree
     * @param newTree
     * @return
     */
    public Node<File> filterImages( Node<File> tree, Node<File> newTree ) { //TODO
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



    /**
     * Iterates through the list of nodes from the model (not a tree) and loads the images to their parent directory nodes.
     * @param
     * @return
     */
    public void loadSprites( String absoluteSourceDensityPath ) {
        List<Node<File>> directories = model.getDirectories();
        File[] files = null;
        for( Node<File> directory : directories ) {
            String finalPath = "";
            if( absoluteSourceDensityPath!=null ) {
                String str = directory.getData().getAbsolutePath(); //absolute path of image directory
                String sourceParent = absoluteSourceDensityPath.substring(0, absoluteSourceDensityPath.lastIndexOf("\\")); //cut density directory
                String strBody = "";
                if( sourceParent.length() < str.length() ) strBody = str.substring( sourceParent.length() ); //cut beginning from image directory until its density folder.
                //replace density with new one.
                String[] split = strBody.split("\\\\");
                if( split.length>0 ) {
                    String body = "";
                    for( int i=2; i<split.length; i++ ) {
                        body += "\\"+split[i];
                    }
                    finalPath += absoluteSourceDensityPath + body;
                    System.out.println( finalPath );
                }
                File tmp = new File( finalPath );
                files = tmp.listFiles( filter_png );
                directory.setData( tmp );
            }else {
                finalPath = directory.getData().getAbsolutePath();
                files = directory.getData().listFiles( filter_png );
            }


            List<String> filenames = getSortedFileNames( files );
            System.out.println("Directory: " + directory.getData().getName() + "; Files found: " + filenames.size() );
            for( int i=0; i<filenames.size(); i++ ) {
                String name = filenames.get(i);
                File file = new File( finalPath + File.separator + name );
                BufferedImage img = null;
                try{
                    img = ImageIO.read( file );
                }catch ( IOException e ) {
                }
                if( img != null ) directory.addFile( new Node<BufferedImage>(img, Node.FileType.FILE) );
            }
        }
        System.out.println("Finished loading sprites.");

         //TODO: return unnecessary??
    }



    public List<String> getSortedFileNames( File[] files ) {
        List<String> filenames = new ArrayList<>();
        if( files != null ) {
            for (File file : files) {
                filenames.add(file.getName());
            }
            Collections.sort(filenames);
        }
        return filenames;
    }




    public void saveSpriteSheets() {
        List<Node<File>> directories = model.getDirectories();

        dialog = new SettingsDialog( this );
        resetCurrentNodeIndex();

    }

    public void nextDirectory( ) {
        boolean k=true;
        do {
            k=model.incDirectoryIndex();
        } while ( !model.getCurrentDirectory().hasFiles() && k );
        System.out.println("directory Index: " + model.getCurrentDirectoryIndex());
        //exportPrompt.next(model.getCurrentDirectory(), model.getCurrentDirectoryIndex() == model.getDirectories().size()-1 );

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
        for( Node<File> node : model.getDirectories() ) {
            if( !node.isCustomFilterChanged() ) node.setCustomFilterValue(value);
        }
    }

    public SGModel.FileFilterState getFfState() {
        return model.getFfstate();
    }

    public String getCustomFilterValue() {
        return ""+model.getCustomFilterValue();
    }

    public Node<File> getCurrentNode() {
        return model.getCurrentDirectory();
    }

    public void previousDirectory() {
        /*if( model.getCurrentDirectoryIndex()==0 ) {
            exportPrompt.setVisible(false);
        }else {
            */do {
                model.decDirectoryIndex();
            } while (!model.getCurrentDirectory().hasFiles() && model.getCurrentDirectoryIndex() >= 0);
            /*
            exportPrompt.next(model.getCurrentDirectory(), model.getCurrentDirectoryIndex() == model.getDirectories().size() - 1);
        }*/
    }

    public void finallyExport() {

        //List<String> densityNames = new ArrayList<>();
        List<String> densityPaths = new ArrayList<>();
        int z=1;
        if( view.isDensityChecked() ) {
            //TODO load new densities and make a new loop
            //for now: folder must have same structure.
            File srcdir = model.getTree().getData();
            srcdir = srcdir.getParentFile();
            File[] densityFiles = srcdir.listFiles();
            for( File f : densityFiles ) {
                //densityNames.add( f.getName() );
                densityPaths.add( f.getAbsolutePath() );
            }
        }

        int d=0;
        while( !densityPaths.isEmpty() ) {
            for (Node<File> directory : model.getDirectories()) {
                if (directory.hasFiles() && directory.isActive()) {
                    System.out.println("Active Spritesheet loading...");

                    int cumulativeWidth = 0;
                    float startOffset = 0;
                    float customIndex = 0;
                    float nte = (float) directory.getFileAmount() / (float) directory.getCustomFilterValue();
                    if (nte < 1) nte = 1;

                    startOffset = (nte - 1) / 2;
                    customIndex += startOffset;
                    System.out.println("Custom Index = " + customIndex + "; nte = " + nte);


                    List<BufferedImage> images = new ArrayList<>();
                    if (directory.firstAlwaysActive() && customIndex >= 1) {
                        BufferedImage image = (BufferedImage) directory.getFileAt(0).getData();
                        images.add(image);
                        cumulativeWidth += image.getWidth();
                    }
                    boolean lastAdded = false;
                    int i = (int) customIndex;
                    for (; i < directory.getFileAmount(); ) {
                        System.out.println("Active Spritesheet now loads the individual images: " + i);
                        BufferedImage image = directory.getFileAt(i).getData();
                        images.add(image);
                        if (i == directory.getFileAmount() - 1) lastAdded = true;
                        cumulativeWidth += image.getWidth();
                        customIndex += nte;
                        i = (int) customIndex;
                    }
                    if (directory.lastAlwaysActive() && !lastAdded) {
                        BufferedImage image = directory.getFileAt(directory.getFileAmount() - 1).getData();
                        images.add(image);
                        cumulativeWidth += image.getWidth();
                    }
                    if (!images.isEmpty()) {
                        BufferedImage finalSpriteSheet = new BufferedImage(cumulativeWidth, images.get(0).getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
                        cumulativeWidth = 0;
                        for (BufferedImage image : images) {
                            finalSpriteSheet.createGraphics().drawImage(image, cumulativeWidth, 0, null);
                            cumulativeWidth += image.getWidth();
                        }

                        String exportPath = directory.getDestinationPath();

                        if(d>0) {
                            String currentDensityPath = densityPaths.get(0);
                            exportPath = getDuplicateDensityExportPath(exportPath, currentDensityPath);
                        }

                        if (!exportPath.endsWith("png")) exportPath += ".png";

                        System.out.println(exportPath);
                        try {
                            ImageIO.write(finalSpriteSheet, "png", new File(exportPath));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Active Spritesheet exported.");
                    }
                }
            }

            //densityPaths = removeDensity( model.getDirectories().get(model.getDirectories().size()-1), densityNames, true );
            if( !densityPaths.isEmpty() ) densityPaths = removeDensity( model.getDirectories().get(model.getDirectories().size()-1), densityPaths );
            if( !densityPaths.isEmpty() ) {
                updateDirectoriesWithNewSrcDensity( densityPaths.get(0) );
                d++;
            }
        }
    }

    public String getDuplicateDensityExportPath( String originalExportPath, String newDensityPath ) {
        if( newDensityPath!=null ) {
            String str = originalExportPath; //absolute path of image directory
            String sourceParent = newDensityPath.substring(0, newDensityPath.lastIndexOf("\\")); //cut density directory
            String strBody = "";
            if( sourceParent.length() < str.length() ) strBody = str.substring( sourceParent.length() ); //cut beginning from image directory until its density folder.
            //replace density with new one.
            String[] split = strBody.split("\\\\");
            if( split.length>0 ) {
                String body = "";
                for( int i=2; i<split.length; i++ ) {
                    body += "\\"+split[i];
                }
                newDensityPath += body;
                System.out.println( newDensityPath );
            }
        }
        return newDensityPath;
    }

    private void updateDirectoriesWithNewSrcDensity( String densityPath ) {
        model.releaseFiles();
        loadSprites( densityPath ); //TODO: debug and test.
    }

    private List<String> removeDensity( Node<File> tree, List<String> names ) {
        int i=0;
        while( !names.isEmpty() && i<names.size() && !names.get(i).equalsIgnoreCase( tree.getData().getAbsolutePath() )) i++;
        //System.out.println( "Names[i]=" + names.get(i) + "; Head of tree: " + tree.getData().getAbsolutePath() );
        names.remove(i);
        return names;
    }

    public void resetCurrentNodeIndex() {
        model.setCurrentDirectoryIndex(0);
    }

    public SGView getView() {
        return view;
    }

    public boolean isLast() {
        return model.getCurrentDirectoryIndex() == model.getDirectories().size()-1;
    }

    public boolean isFirst() {
        return model.getCurrentDirectoryIndex()==0;
    }

    public void setGlobalExportDirectory(String directory) {
        model.setGlobalExportDirectory( directory );
        for( Node<File> node : model.getDirectories() ) {
            if( !node.isCustomDirectoryChanged() ) node.setDestinationPath(directory);
        }
    }

    public String getGlobalExportPath() {
        return view.getGlobalExportPath();
    }

    public String getCurrentIndex() {
        String index = "";
        if( model.getCurrentDirectoryIndex()<10 ) index+="0";
        return index+=model.getCurrentDirectoryIndex();
    }
}
