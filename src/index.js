import { NativeModules } from 'react-native';
const { SpeechModule } = NativeModules;

const Speech = {
  speak(context, callback) {
    let options = {};
    if(context.constructor === String) {
      options.text = context;
      options.language = 'zh-CH';
    } else {
      options = context;
    }
    if (!callback) {
      callback = function callback(argument) {}
    }
    SpeechModule.speak(options, callback);
  },
  stop() {
    SpeechModule.stop();
  }
}

export default Speech;
