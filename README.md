# Time Seeker

Find the earliest, valid 24-hour time (HH:MM:SS) that is possible with the given six digits.

I encountered this deceptively tricky problem during my Montefiore on-site interview on December 3, 2018. It turned out to be similar to the Leet Code _largest hour_ problem that asks for the maximum time using only four digits (hours and minutes) which you may have seen mentioned online.

I struggled with this problem on and off for many weeks after the interview using a brute-force approach which ultimately left me feeling unsatisfied. Recently, I revisited it and came up with a nice clean solution based on the Leet Code algorithm.

During my reevaluation, I recognized that these are the keys to finding a correct and satisfying solution:

1. Trying **every** combination of digits possible because each digit really has a dependency on the others. In other words, once I place a digit using some rule or other logic, I can't know for certain that it will be it's final resting place. Placing it and swapping stuff around or backtracking quickly becomes over complicated.
2. Using hours, minutes and seconds instead of individual digits or locations to validate each possible solution. So, just check that the hours is less than 24 instead of checking if it begins with 2, then the second digit can only be between 0 and 3, etc.
3. Maintaining all possible solutions and determining the final answer **at the end** once all of the digit combinations are exhausted. Don't try to prematurely optimize the algorithm because I don't think there are any short-cuts that work for all cases.

I hope this helps any future time seekers!!!

Time marches on...

Pete Sattler  
February 2022    
_peter@sattler22.net_
