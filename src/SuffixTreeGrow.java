/* Suffix Tree Grow demonstration by Alexandru Ghitza & Philippe-Antoine Warda.
Last modified on March 3, 1997 at 12:30.

For more details, see SuffixTreeGrow.html
Stable version, containing all desired functionality. Implemented screen buffering.
Added possibility of obtaining the label of an edge by left-clicking on its node and the
suffix of a leaf by right-clicking on it.
*/

import java.awt.*;

class STnode {
/* definition of a Suffix Tree node */
  int index;    /* if the node is a leaf, this is its index in the text; must display index+1 (start at 1) */
  String label; /* label of the incoming edge */
  STnode next,  /* next sibling */
  child;      /* first child */
  int level;
  SuffixTreeGrow caller;
  int x, y;

  STnode(int i, String s, int level, SuffixTreeGrow caller) {
  /* constructor method */
    this.index = i;
    this.label = s;
    this.caller = caller;
    this.level = level;
  }

  boolean isLeaf() {
    return(this.child == null);
  }
  
  int children() {
    STnode temp;
    int count = 0;
    if (this.isLeaf()) return(0);
    temp = this.child;
    while (temp != null) {
      count++;
      temp = temp.next;
    }
    return count;
  }

  STnode findNode(int x, int y) {
    if (this.isInside(x, y)) return this;
    STnode temp = this.child;
    while (temp != null) {
      STnode found = temp.findNode(x, y);
      if (found != null) return found;
      temp = temp.next;
    }
    return null;
  }

  boolean isInside(int x, int y) {
    if ((x < this.x)||(x > this.x+2*caller.Display.radius)) return false;
    if ((y < this.y)||(y > this.y+2*caller.Display.radius)) return false;
    return true;
  }

  int depth() {
    int max, current;
    STnode temp;
    if (this.isLeaf()) return(1);
    temp = this.child;
    max = temp.depth();
    while (temp.next != null) {
      temp = temp.next;
      current = temp.depth();
      if (current > max) max = current;
    }
    return(max+1);
  }
  
  int getx() {
    if (!this.isLeaf()) {
      int x1 = this.child.x;
      STnode temp;
      temp = this.child;
      while(temp.next != null)
        temp = temp.next;
      int x2 = temp.x;
      int cx = (x1+x2)/2;
      return(cx);
    }
    return (this.caller.Display.maxx+3*this.caller.Display.radius);
  }
  
  int gety() {
    return (this.level*(caller.Display.edgeLength+2*caller.Display.radius));
  }
  
  int leaves() {
    int sum = 0; 
    STnode temp; 
    if (this.isLeaf()) return(1); 
    temp = this.child; 
    while (temp != null) { 
      sum = sum + temp.leaves();
      temp = temp.next; 
    } 
    return(sum);    
  }
  
  void visit(Graphics g) {
  /* postorder traversal of the Suffix Tree */
    STnode temp;
    if (!this.isLeaf()) {
      temp = this.child;
      while (temp != null) {
        temp.visit(g);
        temp = temp.next;
      }
    }   
    this.x = this.getx();
    if (this.x > caller.Display.maxx) caller.Display.maxx = this.x;
    this.y = this.gety();
    String str = new String((new Integer(this.index+1)).toString());

    g.setColor(Color.red);
    if (this.isLeaf()) g.setColor(Color.green);
    g.fillOval(this.x, this.y, 2*caller.Display.radius, 2*caller.Display.radius);
    g.setColor(Color.black);
    g.drawOval(this.x, this.y, 2*caller.Display.radius, 2*caller.Display.radius);
    if (this.index != -1) {
      FontMetrics fm = g.getFontMetrics(g.getFont());
      int sh = fm.getHeight();
      int sy = (2*this.y+2*caller.Display.radius+sh)/2;
      int sw = fm.stringWidth(str);
      int sx = (2*this.x+2*caller.Display.radius-sw)/2;
      g.drawString(str, sx, sy);
    }
    if (!this.isLeaf()) {
      int x1 = this.x+caller.Display.radius;
      int y1 = this.y+2*caller.Display.radius;
      temp = this.child;
      while (temp != null) {
        int x2 = temp.x+caller.Display.radius;
        int y2 = temp.y;
        g.drawLine(x1, y1, x2, y2);
        int ind = caller.input.indexOf(temp.label)+1;
        int len = ind+temp.label.length()-1;
        String st1 = new String("("+ind+","+len+")");
        int sy = y2-4;
        FontMetrics fm = g.getFontMetrics(g.getFont());
        int sw = fm.stringWidth(st1);
        int sx = (2*temp.x+2*caller.Display.radius-sw)/2;
        int sh = fm.getHeight();
        Color color = g.getColor();
        g.setColor(Color.lightGray);
        g.fillRect(sx-1, sy-sh, sw+1, sh+2);
        g.setColor(color);
        g.drawString(st1, sx, sy);
        temp = temp.next;
      }
    }
    return;
  }

  void insert(int ind, String str, int level) { 
  /* inserts a new node with index ind and label str */
    STnode newnode, temp, prev; /* temporary space to store new node */
    String strtemp, prefix;
    int ii;
    
    if (this.isLeaf()) {
      newnode = new STnode(ind, str, level+1, this.caller);
      this.child = newnode;
      return;
    }     
    temp = this.child;

    if (temp.label.charAt(0) > str.charAt(0)) {
    /* must insert node instead of temp */
      newnode = new STnode(ind, str, level+1, this.caller);
      this.child = newnode;
      newnode.next = temp;
      return;
    }
    prev = temp;
    while ((temp != null) && (temp.label.charAt(0) < str.charAt(0))) {
      prev = temp;
      temp = temp.next;
    }
    if (temp == null) {
    /* add node to end of children list */
      newnode = new STnode(ind, str, level+1, this.caller);
      prev.next = newnode;
      return; 
    }
    if (temp.label.charAt(0) > str.charAt(0)) {
    /* insert node in the list before temp */
      newnode = new STnode(ind, str, level+1, this.caller);
      prev.next = newnode;
      newnode.next = temp;
      return;
    }   
    for (ii = 1; ii < temp.label.length(); ii++)
      if (temp.label.charAt(ii) != str.charAt(ii)) break;

    /* if prefix is exhausted, then insert the node into temp keeping only the suffix */    
    if (ii == temp.label.length()) {
      strtemp = str.substring(ii); /* keep only chars different than temp.label */
      temp.insert(ind, strtemp, level+1);
      return;
    }
    /* must break temp into common prefix and add a subtree with old info */    
    prefix = temp.label.substring(0, ii);
    strtemp = temp.label.substring(ii);
    /* create subtree */
    prev = new STnode(temp.index, strtemp, level+1, this.caller);
    prev.child = temp.child;
    temp.child = prev;
    temp.index = -1;
    temp.label = prefix; 
    /* put entire subtree rooted at prev one level down */
    prev.decreaseLevel();
    /* insert node into temp */
    strtemp = str.substring(ii);
    temp.insert(ind, strtemp, level+1);
    return;   
  }
  
  void decreaseLevel() {
    STnode temp;
    this.level++;
    if (this.isLeaf()) return;
    temp = this.child;
    while (temp != null) {
      temp.decreaseLevel();
      temp = temp.next;
    }
  }
}



class TreeCanvas extends Canvas {
  int radius, edgeLength;
  int maxx;
  int height, width;
  int x;
  SuffixTreeGrow caller;
  Image bufferImage;
  Graphics bufferGraphics;
  
  TreeCanvas(SuffixTreeGrow caller) {
    this.caller = caller;
    this.setBackground(Color.lightGray);
  }

  void coords() {
    this.height = this.size().height;
    this.width = caller.size().width;
    x = caller.bounds().x;
  }
  
  void redraw(int rows, int columns) {
    this.radius = (int)(this.size().width/(3.0*columns+1));
    if ((4*rows*this.radius-2*this.radius) > this.size().height) 
      this.radius = this.size().height/(4*rows-2);
    this.edgeLength = (int)((this.size().height-1-2*this.radius*rows)/(rows-1));
    width = this.radius*(3*columns+1)+1;
    height = this.size().height;
    x = (this.size().width-width)/2;
    this.maxx = -(3*this.radius)/2;
    bufferImage = createImage(width, height);
    this.repaint();
  } 
    
  public void paint(Graphics g) {
    g.clearRect(0, 0, this.size().width, this.size().height);
    if (bufferImage != null)
      g.drawImage(bufferImage, x, 0, this);
  } 

  public void update(Graphics g) {
    if (bufferImage != null) {
      bufferGraphics = bufferImage.getGraphics();
      // clear the image
      bufferGraphics.setColor(Color.lightGray);
      bufferGraphics.fillRect(0, 0, width, height);
      // draw tree
      if (caller.SuffixTree != null) 
        caller.SuffixTree.visit(bufferGraphics);
      // update screen canvas
      g.clearRect(0, 0, this.size().width, this.size().height);
      g.drawImage(bufferImage, x, 0, this);
    }
  }

  public boolean handleEvent(Event evt) {
    if ((evt.id == Event.MOUSE_DOWN) && (caller.SuffixTree != null)) {
			if (evt.modifiers == Event.META_MASK) {
				int x = evt.x - this.x;
				int y = evt.y - caller.bounds().y;
				STnode find = caller.SuffixTree.findNode(x, y);
				if (find == null)
					caller.EdgeLabel.setText("");
				else if (find.index == -1) caller.EdgeLabel.setText("");
					else caller.EdgeLabel.setText("Leaf suffix is: "+caller.input.substring(find.index));
				return true;
      } else if (evt.modifiers != Event.ALT_MASK) {	
      	int x = evt.x - this.x;
      	int y = evt.y - caller.bounds().y;
      	STnode find = caller.SuffixTree.findNode(x, y);
      	if (find == null) 
      		caller.EdgeLabel.setText("");
      	else 
      		caller.EdgeLabel.setText("Edge label is: "+find.label);
      	return true;
      }
    }
    return false;
  }
}



public class SuffixTreeGrow extends java.applet.Applet {
  Button Accept, Next, End;
  TextField Text;
  Label TextLabel, Message, EdgeLabel;
  String input;
  int index;
  STnode SuffixTree;
  TreeCanvas Display;
    
  public void init() {
    resize(600, 400);
    setLayout(new BorderLayout());
    
    Panel input = new Panel();
    TextLabel = new Label("Enter text:");
    input.add(TextLabel);
    Text = new TextField(20);
    input.add(Text);
    Accept = new Button("Accept");
    input.add(Accept);
    add("North", input);
    
    Display = new TreeCanvas(this);
    add("Center", Display);

    Panel output = new Panel();
    output.setLayout(new GridLayout(2,2));
    Next = new Button("Next");
    output.add(Next);
    EdgeLabel = new Label("");
    output.add(EdgeLabel);
    End = new Button("End");
    output.add(End);
    Message = new Label("press Accept to start...");
    output.add(Message);
    add("South", output);
  }
  
  public boolean action(Event evt, Object obj) {
    if (evt.target instanceof Button) {
      String str = (String)obj;
      if (str.equals("Accept")) {
        SuffixTree = new STnode(-1, "", 0, this);
        this.input = (new String(Text.getText()))+"$";
        this.index = 0;
        EdgeLabel.setText("");
        Message.setText("initialization completed! press Next...");
        Display.coords();
        return true;
      }
      if (str.equals("Next")) {
        if (this.index == this.input.length()) {
          Message.setText("tree is complete!");
          return true;
        }
        String str2 = this.input.substring(this.index);
        Message.setText("inserting "+str2);
        SuffixTree.insert(this.index, str2, 0);
        Message.setText("redrawing tree");
        Display.redraw(SuffixTree.depth(), SuffixTree.leaves());
        this.index++;
        Message.setText("press Next to continue...");
        return true;
      }
      if (str.equals("End")) {
        while (this.index != this.input.length()) {
          String str2 = this.input.substring(this.index); 
          Message.setText("inserting "+str2); 
          SuffixTree.insert(this.index, str2, 0); 
          this.index++;
        }
        Message.setText("redrawing tree"); 
        Display.redraw(SuffixTree.depth(), SuffixTree.leaves()); 
        Message.setText("tree is complete!");
        return true;
      }
    }
    return false;
  }
}
