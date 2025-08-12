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

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: ItemRendererModelDummy
 * Created by HellFirePvP
 * Date: 23.07.2016 / 16:21
 */
public class ItemRendererModelDummy implements IModelCustom {

    // Copy-pasta from ModelBakery
    private static final String EMPTY_MODEL_RAW = "{    \'elements\': [        {   \'from\': [0, 0, 0],            \'to\': [16, 16, 16],            \'faces\': {                \'down\': {\'uv\': [0, 0, 16, 16], \'texture\': \'\' }            }        }    ]}"
        .replaceAll("\'", "\"");
    // public static final ModelBlock MODEL_GENERATED = ModelBlock.deserialize(EMPTY_MODEL_RAW);

    private ResourceLocation parent;

    public ItemRendererModelDummy(ResourceLocation parent) {
        this.parent = parent;
    }

    // private static final IModelState NO_STATE = part -> Optional.absent();
    //
    // @Override
    // public Collection<ResourceLocation> getDependencies() {
    // return Collections.emptyList();
    // }
    //
    // @Override
    // public Collection<ResourceLocation> getTextures() {
    // return Collections.emptyList();
    // }
    //
    // @Override
    // public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite>
    // bakedTextureGetter) {
    // return new DummyVanillaBakedModel(getSupportedTransforms());
    // }
    //
    // private ItemCameraTransforms getSupportedTransforms() {
    // ItemCameraTransforms transforms = ItemRenderRegistry.getAdditionalRenderTransforms(parent);
    // return transforms != null ? transforms : MODEL_GENERATED.getAllTransforms();
    // }
    //
    // @Override
    // public IModelState getDefaultState() {
    // return NO_STATE;
    // }
    //
    // static {
    // MODEL_GENERATED.name = "RenderDummyGeneratedBaseModel";
    // }

    @Override
    public String getType() {
        return "";
    }

    @Override
    public void renderAll() {

    }

    @Override
    public void renderOnly(String... groupNames) {

    }

    @Override
    public void renderPart(String partName) {

    }

    @Override
    public void renderAllExcept(String... excludedGroupNames) {

    }
}
