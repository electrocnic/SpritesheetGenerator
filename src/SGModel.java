import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreas on 13.12.2015.
 */
public class SGModel {

    private SpritesheetGenerator controller = null;

    private List<BufferedImage> sprites = null;

    private Node nodes = null;


    public enum FileFilterState {
        ALL,
        EVEN,
        ODD;

        private static FileFilterState[] vals = values();

        public FileFilterState next() {
            return vals[(this.ordinal()+1)%vals.length];
        }
    }

    private FileFilterState ffstate = null;

    private List<Integer> heights = null;
    private List<Integer> widths = null;

    public SGModel( SpritesheetGenerator controller ) {
        this.controller = controller;
        sprites = new ArrayList<BufferedImage>();
        heights = new ArrayList<>();
        widths = new ArrayList<>();
        ffstate = FileFilterState.ALL;
    }

    public int nextState() {
        ffstate = ffstate.next();
        return ffstate.ordinal();
    }

    public FileFilterState getFfstate() {
        return ffstate;
    }

    public void reset() {
        sprites = new ArrayList<BufferedImage>();
        heights = new ArrayList<>();
        widths = new ArrayList<>();
        //ffstate = FileFilterState.ALL;
    }

    public List<BufferedImage> getSprites() {
        return sprites;
    }

    public void setSprites( List<BufferedImage> sprites ) {
        this.sprites = sprites;
    }

    public void addSprite( BufferedImage sprite ) {
        if( sprite!= null ) sprites.add( sprite );
    }

    public void addHeight( int height ) {
        heights.add( height );
    }

    public void addWidth( int width ) {
        widths.add( width );
    }

    public boolean heightsAreEqual() {
        if( heights!=null && !heights.isEmpty() ) {
            int mHeight = heights.get(0);
            for (int height : heights) {
                if( mHeight!=height ) return false;
            }
            return true;
        }
        return false;
    }

    public boolean widthsAreEqual() {
        if( widths != null && !widths.isEmpty() ) {
            int mWidth = widths.get(0);
            for( int width : widths ) {
                if( mWidth != width ) return false;
            }
            return true;
        }
        return false;
    }

    public boolean hasSprites() {
        if( sprites!=null && !sprites.isEmpty() ) {
            return true;
        }
        return false;
    }

    public int getTotalWidth() {
        return getWidthTo( sprites.size() );
    }

    public int getWidthTo( int index ) {
        int totalWidth = 0;
        if( index > sprites.size() ) index = sprites.size();
        if( sprites != null && !sprites.isEmpty() ) {
            for( int i=0; i<index; i++ ) {
                totalWidth += sprites.get(i).getWidth();
            }
            return totalWidth;
        }
        return -1;
    }

    public int getHeight() {
        if( sprites != null && !sprites.isEmpty() ) {
            return sprites.get(0).getHeight();
        }
        return -1;
    }

    public int getType() {
        if( sprites != null && !sprites.isEmpty() ) {
            return sprites.get(0).getType();
        }
        return -1;
    }

    public int getWidth() {
        if( sprites != null && !sprites.isEmpty() ) {
            return sprites.get(0).getWidth();
        }
        return -1;
    }
}
