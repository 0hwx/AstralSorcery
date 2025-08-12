/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.client.util.item;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.IModelCustomLoader;
import net.minecraftforge.client.model.ModelFormatException;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: DummyModelLoader
 * Created by HellFirePvP
 * Date: 23.07.2016 / 16:17
 */
public class DummyModelLoader implements IModelCustomLoader {

    // @Override
    // public boolean accepts(ResourceLocation modelLocation) {
    // return ItemRenderRegistry.isRegistered(modelLocation);
    // }
    //
    // @Override
    // public IModel loadModel(ResourceLocation modelLocation) throws Exception {
    // return new ItemRendererModelDummy(modelLocation);
    // }
    //
    // @Override
    // public void onResourceManagerReload(IResourceManager resourceManager) {}

    @Override
    public String toString() {
        return "IItemRenderer-DummyModelLoader";
    }

    @Override
    public String getType() {
        return "";
    }

    @Override
    public String[] getSuffixes() {
        return new String[0];
    }

    @Override
    public IModelCustom loadInstance(ResourceLocation modelLocation) throws ModelFormatException {
        return new ItemRendererModelDummy(modelLocation);
    }
}
