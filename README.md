# taro-oriented-project
## log
###### tags: `程式專案`,`紫芋導向`,`start travel`
### beta-1.0
> 版本資訊：
> -
> 
> **概述**
> > Auth 讀寫資料庫設定
> > 清單GUI更新
> 
> **類別更動**
> - (新增)**OnOneOffClickListener**:解決listner聽兩次的狀況
> - (微調)**OrdersDBObserver**:吃進來的list移除「已經開始且沒到下限」的order
> - user/orders中的travel跟travels的同步
> 
> **GUI處理類別更動**
> - (微調)**userFrag**:登出功能
> 
> **GUI資料更動**
> - SearchFrag : 改篩選圖示、單項travel變矮、差幾人成團以及已經成團只剩多少名額、不能訂(人滿、過期)灰掉
> - TravelDetail ： 改layout

--- 
> 開發方向
> - 
> - 萃取觀察者模式
> - Invoker實例是否提升層級
> - 整理程式碼 註解
> - 搜尋清單數量問題
> - HomeFrag : ViewPager、ScrollView x2
> - Purchase : 改layout
> - userFrag : 會員部分改窄、會員狀態、登出、單項改layout改資訊量
> - userOrder : 加狀態
> - login,signup ： 改layout?

### alpha-1.9
> 版本資訊：
> -
> 
> **概述**
> > 首頁功能調整
> > 搜尋frag日期按鈕即時更新
> > 登入註冊流程控制
> 
> **類別更動**
> - 完成登入註冊功能的acti生命週期控制
> 
> **GUI處理類別更動**
> - (微調)**首頁frag**:連結搜尋frag跟熱門旅程acti
> - (新增)**首選行程acti**:簡單地用了個全螢幕畫面
> - (新增)**搜尋frag**:日期btn的addTextChangedListener移除im_btn
> - 搜尋frag新增搜尋提醒dialog
> - 新增登入註冊TextWatcher來管控btn
> 
> **GUI資料更動**
> - 更新主畫面資料
> - 首選行程layout
