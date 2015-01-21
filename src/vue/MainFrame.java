package vue;

import controlleur.Controlleur;
import controlleur.action.LoadAction;
import controlleur.action.SaveAction;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ByTeK on 11/11/2014.
 */
public class MainFrame extends JFrame {

    private ToolBarTop toolBarTop;
    private LeftPane leftPane;
    private RightPane rightPane;
    private DrawPanel drawPanel;
    private BottomPanel bottomPanel;
    private Controlleur controlleur;

    public MainFrame(Controlleur controlleur){

        super("BaTVector");
        ImageIcon logo = new ImageIcon("images/logo.jpg");
        this.setIconImage(logo.getImage());
        /*if (System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0){
            com.apple.eawt.Application application = com.apple.eawt.Application.getApplication();
            application.setDockIconImage(logo.getImage());
        }*/
        this.controlleur = controlleur;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle bounds = ge.getMaximumWindowBounds();
        int width = (int)bounds.getWidth();
        int height = (int)bounds.getHeight();

        Container content = getContentPane();




        this.rightPane = createRightPane();
        this.leftPane = createLeftPane();
        this.drawPanel = new DrawPanel(controlleur);
        this.bottomPanel = new BottomPanel(controlleur);
        controlleur.initControlleur(rightPane,drawPanel,leftPane);
        this.toolBarTop = createToolBarTop();

        //this.drawPanel.setBackground(Color.BLUE);

        content.setLayout(new BorderLayout());
        content.add(toolBarTop, BorderLayout.NORTH);
        content.add(leftPane, BorderLayout.WEST);
        content.add(bottomPanel, BorderLayout.SOUTH);
        content.add(rightPane, BorderLayout.EAST);
        content.add(drawPanel, BorderLayout.CENTER);


        this.pack();


        setJMenuBar(createMenuBar());
        setSize(width, height);
        //drawPanel.RAZ();
    }

    protected RightPane createRightPane(){
        RightPane rPane = new RightPane();
        return rPane;
    }

    protected LeftPane createLeftPane(){
        LeftPane lPane = new LeftPane(new JEditorPane(),this.controlleur);

        return lPane;
    }

    protected ToolBarTop createToolBarTop() {
        ToolBarTop bar = new ToolBarTop(drawPanel,controlleur);
        return bar;
    }
    protected JMenuBar createMenuBar() {
        JMenuBar menubar = new JMenuBar();
        JMenu file = new JMenu("Fichier");
        file.add(new SaveAction(controlleur));
        file.add(new LoadAction(controlleur));
        JMenu edit = new JMenu("Edition");
        menubar.add(file);
        menubar.add(edit);
        return menubar;
    }
}
