package com.ifenduo.lib_banner;

import android.support.v4.view.ViewPager.PageTransformer;

import com.ifenduo.lib_banner.transformer.AccordionTransformer;
import com.ifenduo.lib_banner.transformer.BackgroundToForegroundTransformer;
import com.ifenduo.lib_banner.transformer.CubeInTransformer;
import com.ifenduo.lib_banner.transformer.CubeOutTransformer;
import com.ifenduo.lib_banner.transformer.DefaultTransformer;
import com.ifenduo.lib_banner.transformer.DepthPageTransformer;
import com.ifenduo.lib_banner.transformer.FlipHorizontalTransformer;
import com.ifenduo.lib_banner.transformer.FlipVerticalTransformer;
import com.ifenduo.lib_banner.transformer.ForegroundToBackgroundTransformer;
import com.ifenduo.lib_banner.transformer.RotateDownTransformer;
import com.ifenduo.lib_banner.transformer.RotateUpTransformer;
import com.ifenduo.lib_banner.transformer.ScaleInOutTransformer;
import com.ifenduo.lib_banner.transformer.StackTransformer;
import com.ifenduo.lib_banner.transformer.TabletTransformer;
import com.ifenduo.lib_banner.transformer.ZoomInTransformer;
import com.ifenduo.lib_banner.transformer.ZoomOutSlideTransformer;
import com.ifenduo.lib_banner.transformer.ZoomOutTranformer;


public class Transformer {
    public static Class<? extends PageTransformer> Default = DefaultTransformer.class;
    public static Class<? extends PageTransformer> Accordion = AccordionTransformer.class;
    public static Class<? extends PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
    public static Class<? extends PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
    public static Class<? extends PageTransformer> CubeIn = CubeInTransformer.class;
    public static Class<? extends PageTransformer> CubeOut = CubeOutTransformer.class;
    public static Class<? extends PageTransformer> DepthPage = DepthPageTransformer.class;
    public static Class<? extends PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
    public static Class<? extends PageTransformer> FlipVertical = FlipVerticalTransformer.class;
    public static Class<? extends PageTransformer> RotateDown = RotateDownTransformer.class;
    public static Class<? extends PageTransformer> RotateUp = RotateUpTransformer.class;
    public static Class<? extends PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
    public static Class<? extends PageTransformer> Stack = StackTransformer.class;
    public static Class<? extends PageTransformer> Tablet = TabletTransformer.class;
    public static Class<? extends PageTransformer> ZoomIn = ZoomInTransformer.class;
    public static Class<? extends PageTransformer> ZoomOut = ZoomOutTranformer.class;
    public static Class<? extends PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
}
