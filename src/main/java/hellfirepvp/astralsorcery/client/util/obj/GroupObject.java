/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.client.util.obj;

import net.minecraft.client.renderer.Tessellator;
//import net.minecraft.client.renderer.VertexBuffer;
//import net.minecraft.client.renderer.vertex.VertexFormat;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;

/**
 * HellFirePvP@Admin
 * Date: 15.06.2015 / 00:10
 * on WingsExMod
 * GroupObject
 */
public class GroupObject {
    public String name;
    public ArrayList<Face> faces = new ArrayList<Face>();
    public int glDrawingMode;

    public GroupObject() {
        this("");
    }

    public GroupObject(String name) {
        this(name, -1);
    }

    public GroupObject(String name, int glDrawingMode) {
        this.name = name;
        this.glDrawingMode = glDrawingMode;
    }

    @SideOnly(Side.CLIENT)
    public void render() {
        if (faces.size() > 0) {
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawing(glDrawingMode);
            render(tessellator);
            tessellator.draw();
//            VertexBuffer vb = Tessellator.getInstance().getBuffer();
//            vb.begin(glDrawingMode, vf);
//            render(vb);
//            Tessellator.getInstance().draw();
        }
    }

    @SideOnly(Side.CLIENT)
    public void render(Tessellator tess) {
        if (faces.size() > 0) {
            for (Face face : faces) {
                face.addFaceForRender(tess);
            }
        }
    }
}
