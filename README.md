# native-speech

native-speech是使用手机系统自带的tts功能合成语音的组件，可将输入的文本转为语音播放。

## Install

通过npm安装，并通过react-native-cli将原生组件链接至你的工程。

```
npm install native-speech --save
react-native link
```

## Usage

### 可用方法

* `speak(context, callback)`: 播放语音方法。。
  - `context`(String or Object): context为要播放的内容，可以是一个字符串或者一个对象，当是一个字符串时，则默认以中文播放当前字符串。如果是一个对象，则根据对象的key进行播放。对象包含的key有：
    - `text`: 需要播放的文本内容
    - `language`: 需要以哪种语言播放

```js
import Speech from 'native-speech';

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
