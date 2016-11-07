//
//  speechModule.m
//  speech
//
//  Created by Arron on 16/11/2.
//  Copyright © 2016年 somonus. All rights reserved.
//

#import "speechModule.h"
#import "RCTUtils.h"
#import "RCTLog.h"

@implementation speechModule


RCT_EXPORT_MODULE(SpeechModule)

// Speak
RCT_EXPORT_METHOD(speak:(NSDictionary *)args callback:(RCTResponseSenderBlock)callback)
{
    // Error if self.synthesizer was already initialized
    if (self.synthesizer) {
        return callback(@[RCTMakeError(@"There is a speech in progress.  Use the `paused` method to know if it's paused.", nil, nil)]);
    }
    
    // Set args to variables
    NSString *text = args[@"text"];
    NSString *lang = args[@"language"] ? args[@"language"] : @"zh-CH";
    NSNumber *rate = args[@"rate"];
    
    // Error if no text is passed
    if (!text) {
        RCTLogError(@"[Speech] You must specify a text to speak.");
        return;
    }
    
    // Setup utterance and voice
    AVSpeechUtterance *utterance = [[AVSpeechUtterance alloc] initWithString:text];
    
    utterance.voice = [AVSpeechSynthesisVoice voiceWithLanguage:lang];
    
    if (rate) {
        utterance.rate = [rate doubleValue];
    }
    
    self.synthesizer = [[AVSpeechSynthesizer alloc] init];
    self.synthesizer.delegate = self;
    
    // Speak
    [self.synthesizer speakUtterance:utterance];
    
    _callback = callback;
}

// Stops synthesizer
RCT_EXPORT_METHOD(stop)
{
    if (self.synthesizer) {
        [self.synthesizer stopSpeakingAtBoundary:AVSpeechBoundaryImmediate];
    }
}

// Pauses synthesizer
RCT_EXPORT_METHOD(pauseSpeaking)
{
    if (self.synthesizer) {
        [self.synthesizer pauseSpeakingAtBoundary:AVSpeechBoundaryImmediate];
    }
}

// Resumes synthesizer
RCT_EXPORT_METHOD(continueSpeaking)
{
    if (self.synthesizer) {
        [self.synthesizer continueSpeaking];
    }
}

// Returns false if synthesizer is paued
RCT_EXPORT_METHOD(isPaused:(RCTResponseSenderBlock)callback)
{
    if (self.synthesizer.paused) {
        callback(@[@true]);
    } else {
        callback(@[@false]);
    }
}

// Returns true if synthesizer is speaking
RCT_EXPORT_METHOD(isSpeaking:(RCTResponseSenderBlock)callback)
{
    if (self.synthesizer.speaking) {
        callback(@[@true]);
    } else {
        callback(@[@false]);
    }
}

// Returns available voices
RCT_EXPORT_METHOD(speechVoices:(RCTResponseSenderBlock)callback)
{
    NSArray *speechVoices = [AVSpeechSynthesisVoice speechVoices];
    NSArray *locales = [speechVoices valueForKey:@"language"];
    
    callback(@[[NSNull null], locales]);
}

// Delegate

// Finished Handler
-(void)speechSynthesizer:(AVSpeechSynthesizer *)synthesizer didFinishSpeechUtterance:(AVSpeechUtterance *)utterance
{
    self.synthesizer = nil;
    _callback(@[@true]);
}

@end
