# DebounceFunction
## 去抖函数
- 在去抖时间内 执行结果函数
  - 开始去抖时间与当前时间的差异小于最大去抖时间 则最终只会执行一次

- 如果在去抖时间内 执行结果函数
  - 开始去抖时间与当前时间的差异大于等于去抖时间 则会立即执行一次 开始去抖时间则会重置
