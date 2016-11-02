# native-speech

native-speech是使用手机系统自带的tts功能合成语音的组件，可将输入的文本转为语音播放。

## Install

通过npm安装，并通过react-native-cli将原生组件链接至你的工程。

```
npm install native-speech --save
react-native link
```

## Usage

###用法

```js
import Speech from 'react-native-speech';

Speech.speak('测试语音', () => alert('callback'));

```

##Example

*run demo*

```
cd example
npm install
react-native link
npm start
```

### IOS

打开ios目录下的xcode工程，点击run

### Android

使用Android studio打开Android目录，点击run

## License

native-speech is released under the MIT license.
