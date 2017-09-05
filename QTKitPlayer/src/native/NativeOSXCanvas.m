

#import <Cocoa/Cocoa.h>
#import <Quartz/Quartz.h>
//#import <QTKit/QTKit.h>
#import <jawt_md.h>
#import <JavaNativeFoundation/JavaNativeFoundation.h>
#import <AVFoundation/AVFoundation.h>

//#include "org_datavyu_plugins_nativeosx_NativeOSXPlayer.h"
/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_datavyu_plugins_nativeosx_NativeOSXPlayer */

#ifndef _Included_org_datavyu_plugins_nativeosx_NativeOSXPlayer
#define _Included_org_datavyu_plugins_nativeosx_NativeOSXPlayer
#ifdef __cplusplus
extern "C" {
#endif
#undef org_datavyu_plugins_nativeosx_NativeOSXPlayer_FOCUS_TRAVERSABLE_UNKNOWN
#define org_datavyu_plugins_nativeosx_NativeOSXPlayer_FOCUS_TRAVERSABLE_UNKNOWN 0L
#undef org_datavyu_plugins_nativeosx_NativeOSXPlayer_FOCUS_TRAVERSABLE_DEFAULT
#define org_datavyu_plugins_nativeosx_NativeOSXPlayer_FOCUS_TRAVERSABLE_DEFAULT 1L
#undef org_datavyu_plugins_nativeosx_NativeOSXPlayer_FOCUS_TRAVERSABLE_SET
#define org_datavyu_plugins_nativeosx_NativeOSXPlayer_FOCUS_TRAVERSABLE_SET 2L
#undef org_datavyu_plugins_nativeosx_NativeOSXPlayer_TOP_ALIGNMENT
#define org_datavyu_plugins_nativeosx_NativeOSXPlayer_TOP_ALIGNMENT 0.0f
#undef org_datavyu_plugins_nativeosx_NativeOSXPlayer_CENTER_ALIGNMENT
#define org_datavyu_plugins_nativeosx_NativeOSXPlayer_CENTER_ALIGNMENT 0.5f
#undef org_datavyu_plugins_nativeosx_NativeOSXPlayer_BOTTOM_ALIGNMENT
#define org_datavyu_plugins_nativeosx_NativeOSXPlayer_BOTTOM_ALIGNMENT 1.0f
#undef org_datavyu_plugins_nativeosx_NativeOSXPlayer_LEFT_ALIGNMENT
#define org_datavyu_plugins_nativeosx_NativeOSXPlayer_LEFT_ALIGNMENT 0.0f
#undef org_datavyu_plugins_nativeosx_NativeOSXPlayer_RIGHT_ALIGNMENT
#define org_datavyu_plugins_nativeosx_NativeOSXPlayer_RIGHT_ALIGNMENT 1.0f
#undef org_datavyu_plugins_nativeosx_NativeOSXPlayer_serialVersionUID
#define org_datavyu_plugins_nativeosx_NativeOSXPlayer_serialVersionUID -7644114512714619750LL
#undef org_datavyu_plugins_nativeosx_NativeOSXPlayer_serialVersionUID
#define org_datavyu_plugins_nativeosx_NativeOSXPlayer_serialVersionUID -2284879212465893870LL
    /*
     * Class:     org_datavyu_plugins_nativeosx_NativeOSXPlayer
     * Method:    addNativeCoreAnimationLayer
     * Signature: ()V
     */
    JNIEXPORT void JNICALL Java_org_datavyu_plugins_nativeosx_NativeOSXPlayer_addNativeOSXCoreAnimationLayer
    (JNIEnv *, jobject, jstring);
    
    /*
     * Class:     org_datavyu_plugins_nativeosx_NativeOSXPlayer
     * Method:    stop
     * Signature: ()V
     */
    JNIEXPORT void JNICALL Java_org_datavyu_plugins_nativeosx_NativeOSXPlayer_stop
    (JNIEnv *, jobject, jint);
    
    /*
     * Class:     org_datavyu_plugins_nativeosx_NativeOSXPlayer
     * Method:    play
     * Signature: ()V
     */
    JNIEXPORT void JNICALL Java_org_datavyu_plugins_nativeosx_NativeOSXPlayer_play
    (JNIEnv *, jobject, jint);
    
    /*
     * Class:     org_datavyu_plugins_nativeosx_NativeOSXPlayer
     * Method:    setTime
     * Signature: (J)V
     */
    JNIEXPORT void JNICALL Java_org_datavyu_plugins_nativeosx_NativeOSXPlayer_setTime
    (JNIEnv *, jobject, jlong, jint);
    
    /*
     * Class:     org_datavyu_plugins_nativeosx_NativeOSXPlayer
     * Method:    setTimePrecise
     * Signature: (J)V
     */
    JNIEXPORT void JNICALL Java_org_datavyu_plugins_nativeosx_NativeOSXPlayer_setTimePrecise
    (JNIEnv *, jobject, jlong, jint);
    
    /*
     * Class:     org_datavyu_plugins_nativeosx_NativeOSXPlayer
     * Method:    setTimeModerate
     * Signature: (J)V
     */
    JNIEXPORT void JNICALL Java_org_datavyu_plugins_nativeosx_NativeOSXPlayer_setTimeModerate
    (JNIEnv *, jobject, jlong, jint);
    
    /*
     * Class:     org_datavyu_plugins_nativeosx_NativeOSXPlayer
     * Method:    setVolume
     * Signature: (F)V
     */
    JNIEXPORT void JNICALL Java_org_datavyu_plugins_nativeosx_NativeOSXPlayer_setVolume
    (JNIEnv *, jobject, jfloat, jint);
    
    /*
     * Class:     org_datavyu_plugins_nativeosx_NativeOSXPlayer
     * Method:    loadMovie
     * Signature: (Ljava/lang/String;)V
     */
    JNIEXPORT void JNICALL Java_org_datavyu_plugins_nativeosx_NativeOSXPlayer_loadMovie
    (JNIEnv *, jobject, jstring, jint);
    
    JNIEXPORT void JNICALL Java_org_datavyu_plugins_nativeosx_NativeOSXPlayer_release
    (JNIEnv *, jobject, jint);
    
    JNIEXPORT jdouble JNICALL Java_org_datavyu_plugins_nativeosx_NativeOSXPlayer_getMovieWidth
    (JNIEnv *env, jobject canvas, jint movieId);
    
#ifdef __cplusplus
}
#endif
#endif



// forward declaration
@interface MovieLayer : CALayer { }
- (id) initWithFile:(NSString*)file;
@end

NSMutableArray* movies;
NSMutableArray* movieLayers;
NSMutableArray* fpses;

AVPlayer *GetQtMovie(jint idx) {
//    NSLog(@"Getting movie at idx: %d, currentCount: %lu", (int)idx, (unsigned long)[movies count]);
    AVPlayer *pointer = [movies objectAtIndex:(int)idx];
    return pointer;
}

MovieLayer *GetMovieLayer(jint idx) {
    NSLog(@"Getting movieLayer at idx: %d, currentCount: %lu", (int)idx, (unsigned long)[movieLayers count]);

    return [movieLayers objectAtIndex:(int)idx];
}

NSString *ConvertToNSString(JNIEnv *env, jstring str)
{
    if (str == NULL)
    {
        return NULL;
    }
    
    const jchar *chars = (*env)->GetStringChars(env, str, NULL);
    NSString *myNSString =
    [NSString stringWithCharacters:(UniChar *)chars  length:(*env)->GetStringLength(env, str)];
    (*env)->ReleaseStringChars(env, str, chars);
    
    return myNSString;
}

JNIEXPORT jdouble JNICALL Java_org_datavyu_plugins_nativeosx_NativeOSXPlayer_getMovieHeight
(JNIEnv *env, jobject canvas, jint movieId) {
#ifdef JAWT_MACOSX_USE_CALAYER // Java for Mac OS X 10.6 Update 4 or later required
    
    JNF_COCOA_ENTER(env);
    
    
    NSSize sourceSize = GetQtMovie(movieId).currentItem.asset.naturalSize;
    JNF_CHECK_AND_RETHROW_EXCEPTION(env);
    
    return sourceSize.height;
    
    JNF_COCOA_EXIT(env);
    
#endif // JAWT_MACOSX_USE_CALAYER
}

JNIEXPORT jlong JNICALL Java_org_datavyu_plugins_nativeosx_NativeOSXPlayer_getCurrentTime
(JNIEnv *env, jobject canvas, jint movieId) {
#ifdef JAWT_MACOSX_USE_CALAYER // Java for Mac OS X 10.6 Update 4 or later required
    
    JNF_COCOA_ENTER(env);
    
    CMTime newQTTime = GetQtMovie(movieId).currentTime;
    JNF_CHECK_AND_RETHROW_EXCEPTION(env);

    long long time = (newQTTime.cellValue * 1000.0f) / newQTTime.timescale;
    JNF_CHECK_AND_RETHROW_EXCEPTION(env);

    
    return time;
    JNF_COCOA_EXIT(env);
    
#endif // JAWT_MACOSX_USE_CALAYER
}

JNIEXPORT jlong JNICALL Java_org_datavyu_plugins_nativeosx_NativeOSXPlayer_getDuration
(JNIEnv *env, jobject canvas, jint movieId) {
#ifdef JAWT_MACOSX_USE_CALAYER // Java for Mac OS X 10.6 Update 4 or later required
    
    JNF_COCOA_ENTER(env);
    
    
    CMTime newQTTime = GetQtMovie(movieId).currentItem.duration;
    JNF_CHECK_AND_RETHROW_EXCEPTION(env);

    long long time = (newQTTime.cellValue * 1000.0f) / newQTTime.timescale;
    JNF_CHECK_AND_RETHROW_EXCEPTION(env);

    
    return time;
    JNF_COCOA_EXIT(env);
    
#endif // JAWT_MACOSX_USE_CALAYER
}

JNIEXPORT jdouble JNICALL Java_org_datavyu_plugins_nativeosx_NativeOSXPlayer_getMovieWidth
(JNIEnv *env, jobject canvas, jint movieId) {
#ifdef JAWT_MACOSX_USE_CALAYER // Java for Mac OS X 10.6 Update 4 or later required
    
    JNF_COCOA_ENTER(env);
    NSLog(@"Width func");
    fflush(stderr);
    NSLog(@"Number of movies found: %lu", (unsigned long)[movies count]);
    fflush(stderr);
    NSSize sourceSize = GetQtMovie(movieId).currentItem.asset.naturalSize;
    JNF_CHECK_AND_RETHROW_EXCEPTION(env);

    
    return sourceSize.width;
    
    JNF_COCOA_EXIT(env);
    
#endif // JAWT_MACOSX_USE_CALAYER
}

JNIEXPORT jboolean JNICALL Java_org_datavyu_plugins_nativeosx_NativeOSXPlayer_isPlaying
(JNIEnv *env, jobject canvas, jint movieId) {
#ifdef JAWT_MACOSX_USE_CALAYER // Java for Mac OS X 10.6 Update 4 or later required
    
    JNF_COCOA_ENTER(env);
    
    if([GetQtMovie(movieId) rate] != 0) {
        JNF_CHECK_AND_RETHROW_EXCEPTION(env);
        return false;
    }
    else
        return true;

    
    JNF_COCOA_EXIT(env);
    
#endif // JAWT_MACOSX_USE_CALAYER
}

JNIEXPORT jfloat JNICALL Java_org_datavyu_plugins_nativeosx_NativeOSXPlayer_getRate
(JNIEnv *env, jobject canvas, jint movieId) {
#ifdef JAWT_MACOSX_USE_CALAYER // Java for Mac OS X 10.6 Update 4 or later required
    
    JNF_COCOA_ENTER(env);
    
    return [GetQtMovie(movieId) rate];
    JNF_CHECK_AND_RETHROW_EXCEPTION(env);

    
    JNF_COCOA_EXIT(env);
    
#endif // JAWT_MACOSX_USE_CALAYER
}

JNIEXPORT void JNICALL Java_org_datavyu_plugins_nativeosx_NativeOSXPlayer_setRate
(JNIEnv *env, jobject canvas, jfloat rate, jint movieId) {
#ifdef JAWT_MACOSX_USE_CALAYER // Java for Mac OS X 10.6 Update 4 or later required
    
    JNF_COCOA_ENTER(env);
    
    if(rate > 0.0 && rate < 2.0) {
        for (AVPlayerItemTrack *track in GetQtMovie(movieId).currentItem.tracks)
        {
            if ([track.assetTrack.mediaType isEqual:AVMediaTypeAudio])
            {
                track.enabled = true;
            }
        }
    } else {
        for (AVPlayerItemTrack *track in GetQtMovie(movieId).currentItem.tracks)
        {
            if ([track.assetTrack.mediaType isEqual:AVMediaTypeAudio])
            {
                track.enabled = false;
            }
        }
    }
    
    [GetQtMovie(movieId) setRate:rate];
    JNF_CHECK_AND_RETHROW_EXCEPTION(env);

    
    JNF_COCOA_EXIT(env);
    
#endif // JAWT_MACOSX_USE_CALAYER
}

JNIEXPORT jfloat JNICALL Java_org_datavyu_plugins_nativeosx_NativeOSXPlayer_getFPS
(JNIEnv *env, jobject canvas, jfloat rate, jint movieId) {
#ifdef JAWT_MACOSX_USE_CALAYER // Java for Mac OS X 10.6 Update 4 or later required
    
    JNF_COCOA_ENTER(env);
    float fps = 0.00;
    for (AVPlayerItemTrack *track in GetQtMovie(movieId).currentItem.tracks) {
        if ([track.assetTrack.mediaType isEqualToString:AVMediaTypeVideo]) {
            //            fps = track.currentVideoFrameRate;
            fps = track.assetTrack.nominalFrameRate;
        }
    }
    JNF_CHECK_AND_RETHROW_EXCEPTION(env);
    return fps;
//    return [[fpses objectAtIndex:movieId] floatValue];
    
//    for (QTTrack *track in [GetQtMovie(movieId) tracks]) {
//        QTMedia *media = [track media];
//        if (![media hasCharacteristic:QTMediaCharacteristicHasVideoFrameRate])
//            continue;
//        QTTime duration = [(NSValue *)[media attributeForKey:QTMediaDurationAttribute] QTTimeValue];
//        long sampleCount = [(NSNumber *)[media attributeForKey:QTMediaSampleCountAttribute] longValue];
//        return sampleCount * ((NSTimeInterval)duration.timeScale / (NSTimeInterval)duration.timeValue);
//    }
//    return 0.0;
    
    JNF_COCOA_EXIT(env);
    
#endif // JAWT_MACOSX_USE_CALAYER
}

/*
 * Class:     org_datavyu_plugins_nativeosx_NativeOSXPlayer
 * Method:    addNativeCoreAnimationLayer
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_datavyu_plugins_nativeosx_NativeOSXPlayer_addNativeOSXCoreAnimationLayer
(JNIEnv *env, jobject canvas, jstring path) {
#ifdef JAWT_MACOSX_USE_CALAYER // Java for Mac OS X 10.6 Update 4 or later required
	
JNF_COCOA_ENTER(env);
    
    if(!movies) {
        NSLog(@"Initializing movies array");
        movies = [[NSMutableArray alloc] init];
        fpses = [[NSMutableArray alloc] init];
    }
    if(!movieLayers) {
        NSLog(@"Initializing movielayers array");

        movieLayers = [[NSMutableArray alloc] init];
    }
	
    // get the AWT
	JAWT awt;
    awt.version = JAWT_VERSION_1_4 | JAWT_MACOSX_USE_CALAYER; // opt into the CALayer model
    jboolean result = JAWT_GetAWT(env, &awt);
    JNF_CHECK_AND_RETHROW_EXCEPTION(env);
    if (result == JNI_FALSE) return; // CALayer support unavailable prior to Java for Mac OS X 10.6 Update 4
    
    NSString* filePath = ConvertToNSString(env, path);
    
    // get the drawing surface
    JAWT_DrawingSurface *ds = awt.GetDrawingSurface(env, canvas);
    JNF_CHECK_AND_RETHROW_EXCEPTION(env);
    assert(ds != NULL);
    
    // lock the drawing surface
    jint lock = ds->Lock(ds); 
	JNF_CHECK_AND_RETHROW_EXCEPTION(env);
    assert((lock & JAWT_LOCK_ERROR) == 0);
    
    // get the drawing surface info
    JAWT_DrawingSurfaceInfo *dsi = ds->GetDrawingSurfaceInfo(ds);
	JNF_CHECK_AND_RETHROW_EXCEPTION(env);
    
	// Check DrawingSurfaceInfo. This can be NULL on Mac OS X if the native 
	// component heirachy has not been made visible yet on the AppKit thread.
    if (dsi != NULL) {
		// create and attach the layer on the AppKit thread
		[JNFRunLoop performOnMainThreadWaiting:YES withBlock:^(){
			// attach the "root layer" to the AWT Canvas surface layers
			id <JAWT_SurfaceLayers> surfaceLayers = (id <JAWT_SurfaceLayers>)dsi->platformInfo;
            NSLog(@"Attaching movie");
            MovieLayer* movieLayer =[[MovieLayer alloc] initWithFile: filePath];
            JNF_CHECK_AND_RETHROW_EXCEPTION(env);
            NSLog(@"Movie attached");

			surfaceLayers.layer = movieLayer;
		}];
		
        // free the DrawingSurfaceInfo
        ds->FreeDrawingSurfaceInfo(dsi);
        JNF_CHECK_AND_RETHROW_EXCEPTION(env);
    }
    
    NSLog(@"Number of movies found: %lu", (unsigned long)[movies count]);

	
    // unlock the drawing surface
    ds->Unlock(ds); 
    JNF_CHECK_AND_RETHROW_EXCEPTION(env);
    
    // free the drawing surface
    awt.FreeDrawingSurface(ds);
    JNF_CHECK_AND_RETHROW_EXCEPTION(env);
	
JNF_COCOA_EXIT(env);
	
#endif // JAWT_MACOSX_USE_CALAYER
}

JNIEXPORT void JNICALL Java_org_datavyu_plugins_nativeosx_NativeOSXPlayer_stop
(JNIEnv *env, jobject canvas, jint movieId) {
#ifdef JAWT_MACOSX_USE_CALAYER // Java for Mac OS X 10.6 Update 4 or later required
    
    JNF_COCOA_ENTER(env);
    
    NSLog(@"Stop");
    [GetQtMovie(movieId) pause];
    
    JNF_COCOA_EXIT(env);
    
#endif // JAWT_MACOSX_USE_CALAYER
}

JNIEXPORT void JNICALL Java_org_datavyu_plugins_nativeosx_NativeOSXPlayer_play
(JNIEnv *env, jobject canvas, jint movieId) {
#ifdef JAWT_MACOSX_USE_CALAYER // Java for Mac OS X 10.6 Update 4 or later required
    
    JNF_COCOA_ENTER(env);
    
    NSLog(@"Play");
    [GetQtMovie(movieId) play];
    
    JNF_COCOA_EXIT(env);
    
#endif // JAWT_MACOSX_USE_CALAYER
}

JNIEXPORT void JNICALL Java_org_datavyu_plugins_nativeosx_NativeOSXPlayer_setTimePrecise
(JNIEnv *env, jobject canvas, jlong time, jint movieId) {
#ifdef JAWT_MACOSX_USE_CALAYER // Java for Mac OS X 10.6 Update 4 or later required
    
    JNF_COCOA_ENTER(env);
    
    NSLog(@"Setting time %lld", (long long)time);
    if(time == 0) {
        time = 1;
    }
    //    QTTime t = QTMakeTime((long long)time, (long)time);
    
    CMTime newQTTime = [GetQtMovie(movieId) currentTime];
    JNF_CHECK_AND_RETHROW_EXCEPTION(env);
    newQTTime.cellValue = ((long long)time / 1000.0f) * newQTTime.timescale;
    JNF_CHECK_AND_RETHROW_EXCEPTION(env);
    [GetQtMovie(movieId) seekToTime:newQTTime toleranceBefore:kCMTimeZero toleranceAfter:kCMTimeZero];
    
    JNF_CHECK_AND_RETHROW_EXCEPTION(env);
    long long t = (newQTTime.cellValue * 1000.0f) / newQTTime.timescale;
    
    
    JNF_CHECK_AND_RETHROW_EXCEPTION(env);
    
    
    NSLog(@"Reported time %lld", t);
    
    //    [movie stop];
    //    [movie setCurrentTime:t];
    //    [movie play];
    
    JNF_COCOA_EXIT(env);
    
#endif // JAWT_MACOSX_USE_CALAYER
}

JNIEXPORT void JNICALL Java_org_datavyu_plugins_nativeosx_NativeOSXPlayer_setTimeModerate
(JNIEnv *env, jobject canvas, jlong time, jint movieId) {
#ifdef JAWT_MACOSX_USE_CALAYER // Java for Mac OS X 10.6 Update 4 or later required
    
    JNF_COCOA_ENTER(env);
    
    NSLog(@"Setting time %lld", (long long)time);
    if(time == 0) {
        time = 1;
    }
    //    QTTime t = QTMakeTime((long long)time, (long)time);
    CMTime tol = CMTimeMake(500, 1000);
    CMTime newQTTime = [GetQtMovie(movieId) currentTime];
    JNF_CHECK_AND_RETHROW_EXCEPTION(env);
    newQTTime.cellValue = ((long long)time / 1000.0f) * newQTTime.timescale;
    JNF_CHECK_AND_RETHROW_EXCEPTION(env);
    [GetQtMovie(movieId) seekToTime:newQTTime toleranceBefore:tol toleranceAfter:tol];
    
    JNF_CHECK_AND_RETHROW_EXCEPTION(env);
    long long t = (newQTTime.cellValue * 1000.0f) / newQTTime.timescale;
    
    
    JNF_CHECK_AND_RETHROW_EXCEPTION(env);
    
    
    NSLog(@"Reported time %lld", t);
    
    //    [movie stop];
    //    [movie setCurrentTime:t];
    //    [movie play];
    
    JNF_COCOA_EXIT(env);
    
#endif // JAWT_MACOSX_USE_CALAYER
}

JNIEXPORT void JNICALL Java_org_datavyu_plugins_nativeosx_NativeOSXPlayer_setTime
(JNIEnv *env, jobject canvas, jlong time, jint movieId) {
#ifdef JAWT_MACOSX_USE_CALAYER // Java for Mac OS X 10.6 Update 4 or later required
    
    JNF_COCOA_ENTER(env);
    
    NSLog(@"Setting time %lld", (long long)time);
    if(time == 0) {
        time = 1;
    }
//    QTTime t = QTMakeTime((long long)time, (long)time);
    
    CMTime newQTTime = [GetQtMovie(movieId) currentTime];
    JNF_CHECK_AND_RETHROW_EXCEPTION(env);
    newQTTime.cellValue = ((long long)time / 1000.0f) * newQTTime.timescale;
    JNF_CHECK_AND_RETHROW_EXCEPTION(env);
    [GetQtMovie(movieId) seekToTime:newQTTime];

    JNF_CHECK_AND_RETHROW_EXCEPTION(env);
    long long t = (newQTTime.cellValue * 1000.0f) / newQTTime.timescale;

    
    JNF_CHECK_AND_RETHROW_EXCEPTION(env);
    
    
    NSLog(@"Reported time %lld", t);
    
//    [movie stop];
//    [movie setCurrentTime:t];
//    [movie play];
    
    JNF_COCOA_EXIT(env);
    
#endif // JAWT_MACOSX_USE_CALAYER
}

JNIEXPORT void JNICALL Java_org_datavyu_plugins_nativeosx_NativeOSXPlayer_setVolume
(JNIEnv *env, jobject canvas, jfloat volume, jint movieId) {
#ifdef JAWT_MACOSX_USE_CALAYER // Java for Mac OS X 10.6 Update 4 or later required
    
    JNF_COCOA_ENTER(env);
    
    NSLog(@"Setting volume %f", (float)volume);
    [GetQtMovie(movieId) setVolume:(float)volume];
    
    //    [movie stop];
    //    [movie setCurrentTime:t];
    //    [movie play];
    
    JNF_COCOA_EXIT(env);
    
#endif // JAWT_MACOSX_USE_CALAYER
}

JNIEXPORT void JNICALL Java_org_datavyu_plugins_nativeosx_NativeOSXPlayer_release
(JNIEnv *env, jobject canvas, jint movieId) {
#ifdef JAWT_MACOSX_USE_CALAYER // Java for Mac OS X 10.6 Update 4 or later required
    
    JNF_COCOA_ENTER(env);
    
    NSLog(@"Releasing movie");
    [GetQtMovie(movieId) pause];
    [movies replaceObjectAtIndex:GetQtMovie(movieId) withObject:nil];
    
    JNF_COCOA_EXIT(env);
    
#endif // JAWT_MACOSX_USE_CALAYER
}

//@implementation QTKitPlayer
//
//- (id) initWithFile:(NSString*)file {
//    
//}
//
//- (void) play {
//    
//}
//
//- (void) seek {
//    
//}
//
//- (void) stop {
//    
//}
//
//@end


// a "root layer" that contains three sample layers (Quartz Composistion, OpenGL, and QuickTime)
@implementation MovieLayer

- (id) initWithFile:(NSString*)file {
	self = [super init];
	if (!self) return nil;
	
	// instance handles it's own layout
	self.layoutManager = self;
	
//	// create a Quartz Composistion layer from the app bundle
//	NSString *compositionPath = [[NSBundle mainBundle] pathForResource:@"Clouds" ofType:@"qtz"];
//	CALayer *qcLayer = [QCCompositionLayer compositionLayerWithFile:compositionPath];
//	[self addSublayer:qcLayer];
//	
//	// do some custom GL drawing
//	RotatingSquareGLLayer *caGLLayer = [RotatingSquareGLLayer layer];
//	caGLLayer.asynchronous = YES;
//	[self addSublayer:caGLLayer];
	
	// play a QuickTime movie from the app bundle
//	movie = [QTMovie movieNamed:@"Sample.mov" error:nil];
    

    
    
//    NSError* error = nil;
    NSLog(@"Opening %@", file);
    file = [file stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    
    
    // Open the movie in editing mode, get the FPS, close it, open it in playback mode
    AVPlayer *movie = nil;
    NSError *error = nil;
//    NSDictionary *attributes =
//    [NSDictionary dictionaryWithObjectsAndKeys:
//     [NSURL URLWithString:file], QTMovieURLAttribute,
//     [NSNumber numberWithBool:NO], QTMovieLoopsAttribute,
//     [NSNumber numberWithBool:NO], QTMovieOpenForPlaybackAttribute,
//     [NSNumber numberWithBool:NO], QTMovieOpenAsyncOKAttribute,
//     nil];
    
    AVURLAsset *asset = [AVURLAsset assetWithURL:[NSURL URLWithString:file]];
    AVPlayerItem *playerItem = [AVPlayerItem playerItemWithAsset:asset];
    [playerItem addObserver:self forKeyPath:@"status" options:0 context:nil];

//    AVPlayerItem *playerItem = [AVPlayerItem playerItemWithURL: [NSURL URLWithString:file]];
    movie = [AVPlayer playerWithPlayerItem:playerItem];
    [movie addObserver:self forKeyPath:@"status" options:0 context:nil];
//    while(movie.status != AVPlayerStatusUnknown ||
//       movie.currentItem.status != AVPlayerItemStatusReadyToPlay) {
//        NSLog(@"%ld %ld", movie.status, (long)movie.currentItem.status);
//        sleep(1);
//    }
    
    float fps = 0.00;
    for (AVPlayerItemTrack *track in playerItem.tracks) {
        if ([track.assetTrack.mediaType isEqualToString:AVMediaTypeVideo]) {
//            fps = track.currentVideoFrameRate;
            fps = track.assetTrack.nominalFrameRate;
        }
    }
    
    NSSize sourceSize = [[playerItem asset] naturalSize];
    
    
    
    if(movie == nil || error)
        NSLog(@"Error: %@ %@", error, [error userInfo]);
//    NSLog(@"Movie loaded: %lld, %ld", [movie duration].timeValue, [movie duration].timeScale);
    NSLog(@"Playback Height: %f, Width: %f, FPS %f", sourceSize.height, sourceSize.width, fps);

//    NSLog@(@"Movie width: %ld, height: %ld", [movie ])
    
    AVPlayerLayer *avMovieLayer = [AVPlayerLayer playerLayerWithPlayer:movie];
    NSLog(@"Layer created");
    [self addSublayer:avMovieLayer];
    NSLog(@"Added sublayer");
    [movies addObject:movie];
    NSLog(@"Added movie");
    [movieLayers addObject:self];
    [fpses addObject:[NSNumber numberWithFloat:fps]];
    
    NSLog(@"Returning, movie loaded");
	return self;
}

- (void)layoutSublayersOfLayer:(CALayer *)layer {
	NSArray *sublayers = layer.sublayers;
	
	CGRect layerFrame = layer.frame;
	CGFloat width = layerFrame.size.width / [sublayers count];
	CGFloat height = layerFrame.size.height;
	
	// layout left to right
	CGFloat x = 0;
	for(CALayer *child in sublayers) {
		child.frame = CGRectMake(x, 0, width, height);
		x += width;
	}
}

- (void)loadMovie:(NSString*)file {
//    [self removeAllAnimations];
//    for (CALayer *layer in [[self.sublayers copy] autorelease]) {
//        [layer removeFromSuperlayer];
//    }
//    movie = [QTMovie movieWithURL: [NSURL URLWithString:filePath] error:nil];
//    QTMovieLayer* qtMovieLayer = [QTMovieLayer layerWithMovie:movie];
//    [self addSublayer:qtMovieLayer];

}

- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary *)change context:(void *)context
{
    if ([object isKindOfClass:[AVPlayer class]])
    {
        AVPlayer *player = (AVPlayer *)object;
        if ([keyPath isEqualToString:@"status"])
        {   //yes->check it...
            switch(player.status)
            {
                case AVPlayerStatusFailed:
                    NSLog(@"player item status failed");
                    break;
                case AVPlayerStatusReadyToPlay:
                    NSLog(@"player item status is ready to play");
                    // Do some initialization
                    float vol = player.volume;
                    player.volume = 0;
                    [player play];
                    sleep(1);
                    [player pause];
                    [player seekToTime:CMTimeMakeWithSeconds(0.01, 600)];
                    player.volume = vol;
                    break;
                case AVPlayerStatusUnknown:
                    NSLog(@"player item status is unknown");
                    break;
            }
        }

    }
    if ([object isKindOfClass:[AVPlayerItem class]])
    {
        AVPlayerItem *item = (AVPlayerItem *)object;
        //playerItem status cellValue changed?
        if ([keyPath isEqualToString:@"status"])
        {   //yes->check it...
            switch(item.status)
            {
                case AVPlayerItemStatusFailed:
                    NSLog(@"player item status failed");
                    break;
                case AVPlayerItemStatusReadyToPlay:
                    NSLog(@"player item status is ready to play");
                    
                    break;
                case AVPlayerItemStatusUnknown:
                    NSLog(@"player item status is unknown");
                    break;
            }
        }
        else if ([keyPath isEqualToString:@"playbackBufferEmpty"])
        {
            if (item.playbackBufferEmpty)
            {
                NSLog(@"player item playback buffer is empty");
            }
        }
    }
}

@end



