# 맛.zip

## Outline

맛.zip은 사용자의 맛집을 보관하고, 다른 사용자의 맛집 추천 리스트를 보여주는 어플입니다. 

- **나의 음식점** 에서는 나만의 맛집 리스트를 보관하고, 추가 / 삭제를 할 수 있습니다. 또한, 해당 전화번호로 음식점에 전화를 걸 수 있고, 다른 사람들에게 공유하는 기능을 구현하였습니다. 필터 기능을 통해 카테고리 별 음식점을 확인할 수 있습니다.
- **추천 음식점** 에서는 다른 사람들이 추천한 음식점이 화면에 표시되고, 해당 음식점을 클릭할 경우 음식점의 위치가 Google Map을 통해 표시됩니다.
- **음식점 추천하기** 에서는 다른 사람들에게 음식점을 추천할 수 있습니다. **나의 음식점** 에서 항목을 가져올 수 있고, 혹은 새로운 음식점을 추천할 수도 있습니다.

## 기술 스택

`AndroidStudio` `Figma` 

## 탭별 기능 및 구현 기술

### 탭 0: 로그인 화면

- 앱을 실행하면 **Firebase**를 기반으로 한 로그인 화면이 표시되며, 이메일과 비밀번호를 이용해 회원가입 및 로그인을 할 수 있습니다.
- 이때, 이메일 형식을 지키고 6자리 이상의 비밀번호를 사용해야 회원가입이 가능합니다.


https://github.com/user-attachments/assets/30598679-0037-4f9a-bb11-15cee503347e
탭0 : 로그인 화면

### 탭 1 : 나의 음식점

- `restaurants.json` 파일 내 음식점 리스트가 `CustomAdapter` 와 `Fragment1` 을 통해 화면에 표시됩니다. 이때 음식점 종류에 따라 해당 카테고리의 아이콘이 왼쪽에 표시됩니다.
- 필터를 통해 각 카테고리 별 음식점을 볼 수 있습니다.

- **전화 기능(**`call_button.setOnClickListener`)**:**  화면 내 전화 아이콘을 누르면, 전화 어플로 연결되며 저장된 전화번호가 입력됩니다.
- **공유 기능(**`share_button.setOnClickListener`) **:** 화면 내 공유 아이콘을 누르면, 음식점의 이름과 전화번호를 공유할 수 있습니다.

[ 탭1 전화 기능](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/0c00854a-6de3-439b-84e8-f7f57f5d3665/Tab1_PhoneCall.mp4)

 탭1 전화 기능

[탭1  공유 기능](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/7f106b24-a73c-4d01-952f-83d9080f9b82/Tab1_Share.mp4)

탭1  공유 기능

- **추가 기능(**`onRestaurantAdded`)**:** 화면 좌측 상단 `+` 버튼을 누르면, 나의 음식점을 추가할 수 있습니다. 추가된 음식점은 리스트에 추가되며, 선택한 카테고리에 따라 아이콘이 표시됩니다.
- **삭제 기능 (**`deleteRestaurant`**):** 삭제 아이콘을 누르면, 나의 음식점을 삭제할 수 있습니다. 삭제된 음식점은 리스트에서 삭제됩니다.

[탭1 추가 기능](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/393294b9-6c33-4cb4-bade-6cf62255556f/Tab1_AddRestaurant.mp4)

탭1 추가 기능

[탭1 삭제 기능](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/da3a4d49-63b7-4b58-9a6d-388afe3aa948/Tab1_Delete.mp4)

탭1 삭제 기능

### 탭 2 : 추천 음식점

- `restaurants_tab2.json` 파일 내 입력된 음식점 리스트가 `CustomAdapter_tab2` 와 `Fragment2` 을 통해 화면에 구현됩니다.
- 음식점 카테고리 (카페, 주점, 식당)에 따라 배경색이 달라지며, 필터를 통해 특정 카테고리의 음식점만 볼 수 있습니다.
- 음식점 버튼을 누르면, 해당 음식점의 위치를 담은 지도가 `MapFragment` 화면 위에 표시됩니다.

[탭2: 필터 기능](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/5544ba10-1460-44f6-9ff8-9bc64e82a11e/Tab2_Filter.mp4)

탭2: 필터 기능

[탭2: 음식점 위치 확인](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/2ea85352-5872-4c21-9b96-502a843158b4/Tab2_Map.mp4)

탭2: 음식점 위치 확인

### 탭 3 : 음식점 추천하기

- 이미지 업로드 컨테이너를 클릭하면 갤러리에서 이미지를 가져올지, 카메라를 통해 찍은 사진을 사용할지 선택할 수 있습니다.
- `ContextCompat.checkSelfPermission`,`ActivityResultContracts.RequestPermission`을 이용하여 카메라 사용에 권한이 필요한 경우 요청하도록 설정했습니다.
- 추천하고 싶은 음식점의 이름을 입력한 후 해당하는 해시태그를 선택하면 음식점을 다른 사용자에게 추천할 수 있습니다. 추천한 내용은 다른 사용자의 “추천 음식점”에 보여지게 됩니다.
- 이미지, 음식점 이름, 해시태그 중 하나라도 입력되지 않는다면 업로드가 제한됩니다.
- 음식점 이름은 직접 입력할 수도 있고(왼쪽 동영상), “**가져오기**” 버튼을 통해 “나의 음식점”에 등록된 음식점 (`restaurants.json`, `added_restaurants.json`) 중에 선택할 수 있습니다(오른쪽 동영상).

[탭3: 음식점 추가하기 1](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/48005ece-f2b8-4e8b-ac99-839ad0756c38/Tab3_Upload_Open.mp4)

탭3: 음식점 추가하기 1

[탭3: 음식점 추가하기 2](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/e4c39bcd-435e-4614-b223-a0a670eb2e18/Tab3_Upload_Write.mp4)

탭3: 음식점 추가하기 2

### 탭 전환

- 앞서 설명한 탭들을 각각의 `Fragment` 에 구현하였고,  `FragmentContainerView` 와 `androix.navigation` 을 이용해 탭을 디스플레이하고 전환했습니다.
- 버튼을
- 하단 버튼에서 현재 탭을 표시하는 효과가 부드럽게 연결되도록 하기 위해 `FragmentContainerView` 를 제어하는`MainActivity` 에서  `android.animation.ObjectAnimator` 를 활용했습니다.

[화면 전환](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/6d395059-8978-4346-a2d4-a01cba98268c/tab_change.mp4)

화면 전환

## 맛.zip apk 파일

https://drive.google.com/file/d/1iKoOuWe1-wcSX_wITwq1nUrdFfpI7-h7/view?usp=drive_link

# 팀원 소개

### 강건

- 카이스트 전산학부 22학번
- wwsskggn@kaist.ac.kr
- @강건

### 강민우

- 고려대학교 데이터과학과 21학번
- minwookang219@gmai.ocm
- @강민우
